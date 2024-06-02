package com.capgemini.wsb.fitnesstracker.user.internal;

import jakarta.annotation.Nullable;

public record DefaultUser(@Nullable Long id, String firstName, String lastName) {

}
