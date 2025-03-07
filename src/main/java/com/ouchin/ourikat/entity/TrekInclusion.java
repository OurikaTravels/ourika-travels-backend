package com.ouchin.ourikat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trek_inclusions")
@Data
public class TrekInclusion {

    @EmbeddedId
    private TrekInclusionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trekId")
    @JoinColumn(name = "trek_id")
    private Trek trek;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inclusionItemId")
    @JoinColumn(name = "inclusion_item_id")
    private InclusionItem inclusionItem;

    @Column(name = "is_included", nullable = false)
    private boolean isIncluded;
}