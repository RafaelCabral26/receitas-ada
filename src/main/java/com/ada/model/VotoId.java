// src/main/java/com/ada/model/VotoId.java
package com.ada.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VotoId implements Serializable {

    public Long usuarioId;
    public Long receitaId;

    public VotoId() {
    }

    public VotoId(Long usuarioId, Long receitaId) {
        this.usuarioId = usuarioId;
        this.receitaId = receitaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass()!= o.getClass()) return false;
        VotoId votoId = (VotoId) o;
        return Objects.equals(usuarioId, votoId.usuarioId) &&
                Objects.equals(receitaId, votoId.receitaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, receitaId);
    }
}