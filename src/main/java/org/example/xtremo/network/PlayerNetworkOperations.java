/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import org.example.xtremo.custom_exceptions.CustomException;
import org.example.xtremo.handlers.AuthenticationHandler;
import org.example.xtremo.mapper.PlayerMapper;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.model.dto.PlayerScoreDTO;
import org.example.xtremo.model.entity.Game;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.GameType;
import org.example.xtremo.network.protocol.Action;
import static org.example.xtremo.network.protocol.Action.GLOBAL_MESSAGE;
import org.example.xtremo.network.protocol.ActionTypeMapper;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.network.protocol.models.*;


import org.example.xtremo.service.ScoreService;
import org.example.xtremo.session.SessionPlayer;
import org.example.xtremo.service.AuthService;
import org.example.xtremo.service.GameService;
import org.example.xtremo.service.PlayerService;
import org.example.xtremo.session.Session;
import org.example.xtremo.utils.DateTimeGsonAdapter;
import org.example.xtremo.utils.RequestHeaderAdapter;

/**
 *
 * @author wahid
 */
public final class PlayerNetworkOperations {

    private PlayerNetworkOperations() {
        throw new IllegalAccessError();
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(RequestHeader.class, new RequestHeaderAdapter())
            .registerTypeAdapter(LocalDateTime.class, new DateTimeGsonAdapter())
            .create();

    public static Gson getGsonConverter() {
        return gson;
    }

    public static void sendResponse(
            ProtocolMessageEnvelope<?> message,
            DataOutputStream out) throws IOException {
        out.writeUTF(gson.toJson(message));
        out.flush();
    }


    public static void handleClientActionRequest(JsonObject root,
                                           PlayerConnectionHandler client) throws IOException {
        try {
            handleClientAction(root, client);
        }catch (Exception e) {
            sendErrorResponse(e.getMessage(), client);
        }
    }

    private static void sendErrorResponse(String error, PlayerConnectionHandler client)
            throws IOException {
        RequestHeader header = new RequestHeader("JSON", Action.ERROR.name());
        ProtocolMessageEnvelope<ErrorBody> response = new ProtocolMessageEnvelope<>(header,
                new ErrorBody(error));
        sendResponse(response, client.getDos());
    }


    public static void handleClientAction(
            JsonObject root,
            PlayerConnectionHandler client) throws Exception {

        String actionStr = root.getAsJsonObject("header")
                .get("action")
                .getAsString();

        Action action = ActionTypeMapper.getActionType(actionStr);
        AuthService authService = AuthService.getAuthService();

        switch (action) {

            case LOGIN -> {
                ProtocolMessageEnvelope<LoginBody> req = gson.fromJson(root,
                        new TypeToken<ProtocolMessageEnvelope<LoginBody>>() {
                        }.getType());

                PlayerDTO player = AuthenticationHandler.handleLogin(authService, req);

                PlayerScoreDTO playerScoreDTO = ScoreService.getInstance().getPlayerScore(player.id());

                client.setPlayerId(player.id());

                ProtocolMessageEnvelope<PlayerScoreDTO> response = new ProtocolMessageEnvelope<>(
                        new RequestHeader("JSON", Action.LOGIN.name()),
                        playerScoreDTO);

                sendResponse(response, client.getDos());
                Server.logger.info("LOGIN success: " + player.id());
            }

            case REGISTER -> {
                ProtocolMessageEnvelope<RegisterBody> req = gson.fromJson(root,
                        new TypeToken<ProtocolMessageEnvelope<RegisterBody>>() {
                        }.getType());

                PlayerDTO player = AuthenticationHandler.handleRegister(authService, req);

                PlayerScoreDTO playerScoreDTO = ScoreService.getInstance().getPlayerScore(player.id());
                client.setPlayerId(player.id());

                ProtocolMessageEnvelope<PlayerScoreDTO> response = new ProtocolMessageEnvelope<>(
                        new RequestHeader("JSON",  Action.REGISTER.name()),
                        playerScoreDTO);

                sendResponse(response, client.getDos());
            }

            case LOGOUT -> {
                ProtocolMessageEnvelope<LogoutBody> req = gson.fromJson(root,
                        new TypeToken<ProtocolMessageEnvelope<LogoutBody>>() {
                        }.getType());

                AuthenticationHandler.handleLogout(authService, req);

                client.forceDisconnect();
                Server.logger.info("LOGOUT playerId=" + client.getPlayerId());
            }

            case INVITE -> {
                ProtocolMessageEnvelope<InviteBody> req = gson.fromJson(root,
                        new TypeToken<ProtocolMessageEnvelope<InviteBody>>() {
                        }.getType());

                int receiverId = req.getBody().getPlayer2().id();
                PlayerConnectionHandler receiver = Server.activePlayers.get(receiverId);

                if (receiver == null) {
                    throw new CustomException.InviteError("User not active");
                }

                sendResponse(req, receiver.getDos());
            }

            case INVITE_CONFIRMED -> {
                ProtocolMessageEnvelope<InviteConfirmedBody> req = gson.fromJson(root,
                        new TypeToken<ProtocolMessageEnvelope<InviteConfirmedBody>>() {
                        }.getType());

                InviteConfirmedBody body = req.getBody();
                int senderId = body.getSenderId();
                int receiverId = body.getReceiverId();

                PlayerConnectionHandler sender = Server.activePlayers.get(senderId);
                PlayerConnectionHandler receiver = Server.activePlayers.get(receiverId);

                Session playerOneSession = Server.sessionManager.getByPlayer(senderId);
                Session playerTwoSession = Server.sessionManager.getByPlayer(receiverId);

                if(playerTwoSession != null || playerOneSession != null) {
                    throw new CustomException.InviteError("This player is already in a session");
                }
                if (sender == null || receiver == null) {
                    throw new CustomException.InviteError("One of the players is offline");
                }
                SessionPlayer p1 = new SessionPlayer(senderId, sender);
                SessionPlayer p2 = new SessionPlayer(receiverId, receiver);

                String sessionId = senderId + "-" + receiverId;
                Session session = new Session(sessionId, p1, p2);

                Server.sessionManager.register(session);
                Server.logger.info("Session created: " + sessionId);

                PlayerService playerService = PlayerService.getPlayerService();

                RequestHeader header = new RequestHeader("JSON", Action.INVITE_CONFIRMED.name());
                InviteConfirmedResponseBody responseBody = new InviteConfirmedResponseBody();
                Optional<Player> senderPlayer = playerService.findById(senderId);
                Optional<Player> receiverPlayer = playerService.findById(receiverId);

                if (senderPlayer.isPresent()) {
                    Player p = senderPlayer.get();
                    responseBody.setPlayer1(new MovePlayer(p.getUsername(), Symbols.X.name()));
                }
                if (receiverPlayer.isPresent()) {
                    Player p = receiverPlayer.get();
                    responseBody.setPlayer2(new MovePlayer(p.getUsername(), Symbols.O.name()));
                }

                ProtocolMessageEnvelope<InviteConfirmedResponseBody> res = new ProtocolMessageEnvelope<>(header,responseBody);
                session.forward(res, senderId);
                session.forward(res, receiverId);
                
            }
            case SESSION_ENDED -> {
                int playerId = client.getPlayerId();
                Session session = Server.sessionManager.getByPlayer(playerId);
                session.closeSession();
            }

            case SESSION_MESSAGE -> {
                ProtocolMessageEnvelope<SessionMessageBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<SessionMessageBody>>() {
                }.getType());
                int playerId = client.getPlayerId();
                Session session = Server.sessionManager.getByPlayer(playerId);
                GameState gameState = req.getBody().getState();
                if (session == null) {
                    throw new IllegalStateException("Player not in session");
                }
                GameService gameService = GameService.getGameService();
                ScoreService score = ScoreService.getInstance();
                switch (gameState) {
                    case WIN -> {
                        Game game = new Game();
                        game.setGameType(GameType.TIC_TAC_TOE);
                        game.setPlayer1Id(client.getPlayerId());
                        game.setWinnerId(client.getPlayerId());
                        var otherPlayerId = session.getOtherPlayer(playerId).getPlayerId();
                        game.setPlayer2Id(otherPlayerId);
                        game.setGameResult(GameResult.WIN);
                        game.setStartedAt(session.getStartedAt());
                        game.setEndedAt(LocalDateTime.now());
                        gameService.save(game);
                        score.updateGameResult(GameResult.WIN, GameType.TIC_TAC_TOE, client.getPlayerId());
                        score.updateGameResult(GameResult.LOSS, GameType.TIC_TAC_TOE, otherPlayerId);
                    }
                    case DRAW -> {
                        Game game = new Game();
                        game.setGameType(GameType.TIC_TAC_TOE);
                        game.setPlayer1Id(client.getPlayerId());
                        var otherPlayerId = session.getOtherPlayer(playerId).getPlayerId();
                        game.setPlayer2Id(otherPlayerId);
                        game.setGameResult(GameResult.DRAW);
                        game.setStartedAt(session.getStartedAt());
                        game.setEndedAt(LocalDateTime.now());
                        gameService.save(game);
                        score.updateGameResult(GameResult.DRAW, GameType.TIC_TAC_TOE, client.getPlayerId());
                        score.updateGameResult(GameResult.DRAW, GameType.TIC_TAC_TOE, otherPlayerId);
                    }
                    default -> {
                    }
                }

                System.out.println("Forwarding to " + playerId + " " + req);
                session.forward(req, playerId);
            }
            case GET_ACTIVE_USERS -> {
                PlayerService playerService = PlayerService.getPlayerService();

                ProtocolMessageEnvelope<GetActivePlayersBody> response = new ProtocolMessageEnvelope<>();
                response.header = new RequestHeader("JSON", Action.GET_ACTIVE_USERS.name());
                GetActivePlayersBody body = new GetActivePlayersBody();

                Enumeration<Integer> keys = Server.activePlayers.keys();

                while (keys.hasMoreElements()) {
                    Integer nextElement = keys.nextElement();
                    Optional<Player> playerOption = playerService.findById(nextElement);

                    if (playerOption.isPresent()) {
                        Player player = playerOption.get();
                        body.add(player.toPlayerDto());
                    }

                }
                response.setBody(body);
                sendResponse(response, client.getDos());
            }
            case LOBBY -> {
                PlayerService playerService = PlayerService.getPlayerService();
                ScoreService scoreService = ScoreService.getInstance();

                ProtocolMessageEnvelope<LobbyResponse> response = new ProtocolMessageEnvelope<>();
                response.header = new RequestHeader("JSON", Action.LOBBY.name());
                List<PlayerScoreDTO> players = new ArrayList<>();
                Enumeration<Integer> keys = Server.activePlayers.keys();
                while (keys.hasMoreElements()) {
                    Integer nextElement = keys.nextElement();
                    Optional<Player> playerOption = playerService.findById(nextElement);
                    if(playerOption.isEmpty())
                        continue;
                    var playerWithScore = scoreService.getPlayerScore(playerOption.get().getId());
                    players.add(playerWithScore);
                }

                response.setBody(new LobbyResponse(players,scoreService.getAllPlayerScores()));
                sendResponse(response, client.getDos());
            }
            case INVITE_DECLINED -> {
                ProtocolMessageEnvelope<InviteDeclinedBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<InviteDeclinedBody>>() {
                }.getType());
                int reciverId = req.getBody().getRecievrId();
                PlayerConnectionHandler receiverHandler = Server.activePlayers.get(reciverId);
                sendResponse(req, receiverHandler.getDos());
            }
            case IN_GAME_MESSAGE -> {
                ProtocolMessageEnvelope<InGameMessageBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<InGameMessageBody>>() {
                }.getType());
                int senderId = client.getPlayerId();
                Session session = Server.sessionManager.getByPlayer(senderId);
                session.forward(req, senderId);
            }
            case GLOBAL_MESSAGE -> {
                ProtocolMessageEnvelope<GlobalMessageBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<GlobalMessageBody>>() {
                }.getType());

                Server.activePlayers.forEachValue(1, e -> {
                    if (e.getDos() != client.getDos()) {
                        try {
                            sendResponse(req, e.getDos());
                        } catch (IOException ex) {
                            Server.logger.error("Can't reach client " + client.getPlayerId());
                        }
                    }
                });
            }
            case INVITE_ALL -> {
                broadcastInviteToOthers(client.getPlayerId(), Action.INVITE);
            }

            default ->
                throw new AssertionError("Unhandled action: " + action);
        }
    }

    public static void broadcastInviteToOthers(int playerId , Action action) throws Exception {
        var playerService = PlayerService.getPlayerService();

        ProtocolMessageEnvelope<InviteBody> response =   new ProtocolMessageEnvelope<>(
                new RequestHeader("JSON", action.name()), new InviteBody());

        var sender = playerService.findById(playerId)
                .orElseThrow(() -> new IllegalStateException("Player not found: " + playerId));

        Server.activePlayers.forEachValue(1, client -> {
            if (client.getPlayerId() != playerId) {
                try {
                    var receiver = playerService.findById(client.getPlayerId())
                            .orElseThrow(() -> new IllegalStateException("Player not found: " + client.getPlayerId()));
                    response.setBody(new InviteBody(PlayerMapper.toDto(sender), PlayerMapper.toDto(receiver)));

                    sendResponse(response, client.getDos());

                } catch (IOException e) {
                    Server.logger.error("Can't reach client {}" + playerId);
                }
            }
        });
    }

    public static void broadcastToOthers(int playerId , Action action) throws Exception {
        PlayerScoreDTO player = ScoreService.getInstance().getPlayerScore(playerId);


        ProtocolMessageEnvelope<PlayerScoreDTO> response =
                new ProtocolMessageEnvelope<>(
                        new RequestHeader("JSON", action.name()),
                        player);

        Server.activePlayers.forEachValue(1, client -> {
            if (client.getPlayerId() != playerId) {
                try {
                    sendResponse(response, client.getDos());
                } catch (IOException e) {
                    Server.logger.error("Can't reach client {}" + playerId +  e);
                }
            }
        });
    }
}
