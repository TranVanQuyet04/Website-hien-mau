    package com.quyet.superapp.entity.address;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "City")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class City {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "city_id")
        private Long cityId;

        @Column(name = "name_city", columnDefinition = "NVARCHAR(50)")
        private String nameCity;
    }
