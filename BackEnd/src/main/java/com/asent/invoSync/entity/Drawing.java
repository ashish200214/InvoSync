package com.asent.invoSync.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drawing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String drawingUrl;

    @OneToMany(mappedBy = "drawing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> media = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation; // link to quotation (if needed)

    @OneToOne(mappedBy = "drawing")
    private Bill bill;
}
