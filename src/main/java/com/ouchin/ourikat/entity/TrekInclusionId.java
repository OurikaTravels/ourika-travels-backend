package com.ouchin.ourikat.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Objects;

@Embeddable
@Data
public class TrekInclusionId {
    private Long trekId;
    private Long inclusionItemId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrekInclusionId that = (TrekInclusionId) o;
        return Objects.equals(trekId, that.trekId) && Objects.equals(inclusionItemId, that.inclusionItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trekId, inclusionItemId);
    }
}
