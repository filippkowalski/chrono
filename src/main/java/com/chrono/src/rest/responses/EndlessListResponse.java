package com.chrono.src.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Filip Kowalski on 05.03.17.
 */

public class EndlessListResponse<D> {

    @Getter
    private int count;

    @Getter
    private String next;

    @Getter
    private String previous;

    @Getter
    @SerializedName("results")
    private List<D> data = new ArrayList<>();
}