/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.example.xtremo.model.dto.PlayerDTO;

/**
 *
 * @author wahid
 */
public class GetActivePlayersBody implements List<PlayerDTO> {

    private final List<PlayerDTO> activeUsers = new ArrayList<>();

    public GetActivePlayersBody() {
    }

    public List<PlayerDTO> getActiveUsers() {
        return activeUsers;
    }

    @Override
    public int size() {
        return activeUsers.size();
    }

    @Override
    public boolean isEmpty() {
        return activeUsers.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return activeUsers.contains(o);
    }

    @Override
    public Iterator<PlayerDTO> iterator() {

        return activeUsers.iterator();
    }

    @Override
    public Object[] toArray() {
        return activeUsers.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return activeUsers.toArray(a);
    }

    @Override
    public boolean add(PlayerDTO e) {
        return activeUsers.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return activeUsers.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return activeUsers.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PlayerDTO> c) {
        return activeUsers.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends PlayerDTO> c) {
        return activeUsers.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return activeUsers.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return activeUsers.retainAll(c);
    }

    @Override
    public void clear() {
        activeUsers.clear();
    }

    @Override
    public PlayerDTO get(int index) {
        return activeUsers.get(index);
    }

    @Override
    public PlayerDTO set(int index, PlayerDTO element) {
        return activeUsers.set(index, element);
    }

    @Override
    public void add(int index, PlayerDTO element) {
        activeUsers.add(index, element);
    }

    @Override
    public PlayerDTO remove(int index) {
        return activeUsers.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return activeUsers.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return activeUsers.lastIndexOf(o);
    }

    @Override
    public ListIterator<PlayerDTO> listIterator() {
        return activeUsers.listIterator();
    }

    @Override
    public ListIterator<PlayerDTO> listIterator(int index) {
        return activeUsers.listIterator(index);
    }

    @Override
    public List<PlayerDTO> subList(int fromIndex, int toIndex) {
        return activeUsers.subList(fromIndex, toIndex);
    }

}
