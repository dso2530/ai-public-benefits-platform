package com.govtech.profile.domain.valueobject;

public interface Mergeable<T> {

    T merge(T incoming);

}