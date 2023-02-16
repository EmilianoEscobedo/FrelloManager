package com.laingard.FrelloManager.model;

import com.laingard.FrelloManager.enumeration.EState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "Orders")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<OrderProduct> products;
    private String timeStamp;
    private Double totalPrice;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @Enumerated(EnumType.STRING)
    private State state;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    @PrePersist
    protected void onCreate() {
        this.timeStamp = ZonedDateTime.now(ZoneId.of("GMT-3")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }



}
