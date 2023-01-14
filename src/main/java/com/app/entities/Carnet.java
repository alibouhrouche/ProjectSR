package com.app.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carnets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement
@ToString
public class Carnet {

    @EmbeddedId
    private CarnetId id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client",nullable = false,insertable=false, updatable=false)
    @ToString.Exclude
    private Client idClient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sport", nullable = false,insertable=false, updatable=false)
    @ToString.Exclude
    private Sport idSport;

    @Column(name = "nombre_entrees", nullable = false)
    private Integer nombreEntrees;

}