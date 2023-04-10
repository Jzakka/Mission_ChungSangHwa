package com.ll.gramgram.base.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@AllArgsConstructor
public class RsData<T> {
    private String resultCode;
    private String msg;
    private T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public static <T> RsData<T> successOf(T data) {
        return of("S-1", "성공", data);
    }

    public static <T> RsData<T> failOf(T data) {
        return of("F-1", "실패", data);
    }

    public static <T> RsData<T> doingOf() {
        return of("P", "In processing");
    }

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    public boolean isFail() {
        return isSuccess() == false;
    }

    public RsData then(Function<RsData, RsData> constrain) {
        if (this.getResultCode().equals("P")) {
            return constrain.apply(this);
        }
        return this;

    }

    public static <T> RsData<T> produce(Supplier<RsData> callback) {
        return callback.get();
    }
}
