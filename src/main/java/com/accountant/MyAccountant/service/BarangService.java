package com.accountant.MyAccountant.service;

import com.accountant.MyAccountant.entity.Barang;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.repository.BarangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarangService {

private final BarangRepository barangRepository;


    public Barang addBarang(Barang barang) throws AllException {
        for (int i = 0; i < 10; i++) {
            String randomBarang = UUIDGeneratorService.generateBarang();
            barang.setKodebarang(randomBarang);
        }

        if (barang.getNamabarang() == null || barang.getNamabarang().isEmpty()) {
            throw new AllException("Nama barang harus di isi !!!");
        }
        if (barang.getJenisbarang() == null || barang.getJenisbarang().isEmpty()) {
            throw new AllException("Jenis barang harus di isi !!!");
        }
        if (barang.getJumlahbarang() == null || barang.getJumlahbarang().describeConstable().isEmpty()) {
            throw new AllException("Jumlah barang harus di isi !!!");
        }
        if (barang.getHargabarang() == null || barang.getHargabarang().describeConstable().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tglmasuk = Optional.ofNullable(barang.getTglmasuk());
        if (tglmasuk.isPresent()) {
            String tanggalMasuk = tglmasuk.get().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (tanggalMasuk.isEmpty()) {
                throw new AllException("Tanggal masuk harus di isi !!!");
            }
        } else {
            throw new AllException("Tanggal masuk harus di isi !!!");
        }

        return barangRepository.save(barang);
    }


    public List<Barang> listBarang() {

        return barangRepository.findAll();
    }


    public Barang listBarangById(Long id) throws AllException {

        Optional<Barang> barang = barangRepository.findById(id);

        if(!barang.isPresent()){
            throw new AllException("Barang tidak ditemukan");
        }
        return barang.get();
    }


    public void deleteBarangById(Long id) throws AllException {
        boolean exist = barangRepository.existsById(id);
        if(!exist){
            throw new AllException("Barang dengan Id" + id + "tidak ada");
        }
        barangRepository.deleteById(id);

    }


    public Barang updateBarangById(Long id, Barang barang) throws AllException {
        if (barang.getNamabarang() == null || barang.getNamabarang().isEmpty()) {
            throw new AllException("Nama barang harus di isi !!!");
        }
        if (barang.getJenisbarang() == null || barang.getJenisbarang().isEmpty()) {
            throw new AllException("Jenis barang harus di isi !!!");
        }
        if (barang.getJumlahbarang() == null || barang.getJumlahbarang().describeConstable().isEmpty()) {
            throw new AllException("Jumlah barang harus di isi !!!");
        }
        if (barang.getHargabarang() == null || barang.getHargabarang().describeConstable().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tglmasuk = Optional.ofNullable(barang.getTglmasuk());
        if (tglmasuk.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String tanggalMasuk = barang.getTglmasuk().format(formatter);
            if (tanggalMasuk.isEmpty()) {
                throw new AllException("Tanggal masuk harus di isi !!!");
            }
        } else {
            throw new AllException("Tanggal masuk harus di isi !!!");
        }

        Barang updatedBarang = barangRepository.findById(id)
                .orElseThrow(() -> new AllException("Barang dengan Id" + id + "tidak ada"));

        updatedBarang.setNamabarang(barang.getNamabarang());
        updatedBarang.setJenisbarang(barang.getJenisbarang());
        updatedBarang.setJumlahbarang(barang.getJumlahbarang());
        updatedBarang.setHargabarang(barang.getHargabarang());
        updatedBarang.setTglmasuk(barang.getTglmasuk());
        updatedBarang.setImageurl(barang.getImageurl());
        barangRepository.save(updatedBarang);

        return updatedBarang;
    }


    public void deleteBarangByKodeBarang(String kodebarang) throws AllException {
        Optional <Barang> deleteBarang = barangRepository.findBykodebarang(kodebarang);
        if(!deleteBarang.isPresent()){
            throw new AllException("Barang dengan kode barang" + kodebarang + "tidak ada");
        } else {

            barangRepository.deleteBykodebarang(kodebarang);}
    }


    public Barang updateBarangByKodeBarang(String kodebarang, Barang barang) throws AllException {
        if (barang.getNamabarang() == null || barang.getNamabarang().isEmpty()) {
            throw new AllException("Nama barang harus di isi !!!");
        }
        if (barang.getJenisbarang() == null || barang.getJenisbarang().isEmpty()) {
            throw new AllException("Jenis barang harus di isi !!!");
        }
        if (barang.getJumlahbarang() == null || barang.getJumlahbarang().describeConstable().isEmpty()) {
            throw new AllException("Jumlah barang harus di isi !!!");
        }
        if (barang.getHargabarang() == null || barang.getHargabarang().describeConstable().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tglmasuk = Optional.ofNullable(barang.getTglmasuk());
        if (tglmasuk.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String tanggalMasuk = barang.getTglmasuk().format(formatter);
            if (tanggalMasuk.isEmpty()) {
                throw new AllException("Tanggal masuk harus di isi !!!");
            }
        } else {
            throw new AllException("Tanggal masuk harus di isi !!!");
        }

        Barang updatedBarang = barangRepository.findBykodebarang(kodebarang)
                .orElseThrow(() -> new AllException("Barang dengan kode barang" + kodebarang + "tidak ada"));

        updatedBarang.setNamabarang(barang.getNamabarang());
        updatedBarang.setJenisbarang(barang.getJenisbarang());
        updatedBarang.setJumlahbarang(barang.getJumlahbarang());
        updatedBarang.setHargabarang(barang.getHargabarang());
        updatedBarang.setTglmasuk(barang.getTglmasuk());
        updatedBarang.setImageurl(barang.getImageurl());
        barangRepository.save(updatedBarang);

        return updatedBarang;
    }
}
