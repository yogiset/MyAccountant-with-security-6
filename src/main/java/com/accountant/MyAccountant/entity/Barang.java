package com.accountant.MyAccountant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "barang")
public class Barang {
    @Id
    @SequenceGenerator(name = "barang_sequence",sequenceName = "barang_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "barang_sequence")
    private Long id;
    private String kodebarang;
    private String namabarang;
    private String jenisbarang;
    private Integer jumlahbarang;
    private Integer hargabarang;
    @Column(columnDefinition="text")
    private String imageurl;
    private LocalDate tglmasuk;

}
