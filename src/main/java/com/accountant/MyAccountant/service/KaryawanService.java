package com.accountant.MyAccountant.service;

import com.accountant.MyAccountant.entity.Karyawan;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.repository.KaryawanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KaryawanService {

private final KaryawanRepository karyawanRepository;
private String userRole;
    public Karyawan addKaryawan(Karyawan karyawan, Map<String,String> requestMap) throws AllException {
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);


        for (int i = 0; i < 10; i++) {
            String randomKaryawan = UUIDGeneratorService.generateKaryawan();
            karyawan.setKodekaryawan(randomKaryawan);
        }

        if (karyawan.getNama() == null || karyawan.getNama().isEmpty()) {
            throw new AllException("Nama harus di isi !!!");
        }
        if (karyawan.getEmail() == null || karyawan.getEmail().isEmpty()) {
            throw new AllException("Email harus di isi !!!");
        }
        if (karyawan.getJabatan() == null || karyawan.getJabatan().isEmpty()) {
            throw new AllException("Jabatan harus di isi !!!");
        }
        if (karyawan.getPhone() == null || karyawan.getPhone().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tgllahir = Optional.ofNullable(karyawan.getTgl_lahir());
        if (tgllahir.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String tanggalLahir = karyawan.getTgl_lahir().format(formatter);
            if (tanggalLahir.isEmpty()) {
                throw new AllException("Tanggal lahir harus di isi !!!");
            }

        } else {
            throw new AllException("Tanggal lahir harus di isi !!!");
        }


        return karyawanRepository.save(karyawan);
    }


    public List<Karyawan> showAllKaryawan() {
        return karyawanRepository.findAll();
    }


    public Karyawan fetchKaryawanById(Long id) throws AllException {
        Optional<Karyawan> karyawan = karyawanRepository.findById(id);

        if (!karyawan.isPresent()) {
            throw new AllException("Karyawan tidak ditemukan");
        }
        return karyawan.get();
    }


    public void deleteKaryawanById(Long id,Map<String,String> requestMap,String userRole) throws AllException {
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);

        boolean exist = karyawanRepository.existsById(id);
        if (!exist) {
            throw new AllException("karyawan dengan Id" + id + "tidak ada");
        }
        karyawanRepository.deleteById(id);


    }


    public Karyawan updateKaryawan(Long id, Karyawan karyawan,Map<String,String> requestMap,String userRole) throws AllException {
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);

        if (karyawan.getNama() == null || karyawan.getNama().isEmpty()) {
            throw new AllException("Nama harus di isi !!!");
        }
        if (karyawan.getEmail() == null || karyawan.getEmail().isEmpty()) {
            throw new AllException("Email harus di isi !!!");
        }
        if (karyawan.getJabatan() == null || karyawan.getJabatan().isEmpty()) {
            throw new AllException("Jabatan harus di isi !!!");
        }
        if (karyawan.getPhone() == null || karyawan.getPhone().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tgllahir = Optional.ofNullable(karyawan.getTgl_lahir());
        if (tgllahir.isPresent()) {
            String tanggalLahir = tgllahir.get().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (tanggalLahir.isEmpty()) {
                throw new AllException("Tanggal lahir harus di isi !!!");
            }
        } else {
            throw new AllException("Tanggal lahir harus di isi !!!");
        }

        Karyawan updatedKaryawan = karyawanRepository.findById(id)
                .orElseThrow(() -> new AllException("karyawan dengan Id" + id + "tidak ada"));

        updatedKaryawan.setNama(karyawan.getNama());
        updatedKaryawan.setEmail(karyawan.getEmail());
        updatedKaryawan.setJabatan(karyawan.getJabatan());
        updatedKaryawan.setPhone(karyawan.getPhone());
        updatedKaryawan.setImageurl(karyawan.getImageurl());
        updatedKaryawan.setTgl_lahir(karyawan.getTgl_lahir());
        karyawanRepository.save(updatedKaryawan);

        return updatedKaryawan;
    }


    public void deleteKaryawanByKodeKaryawan(String kodekaryawan,Map<String,String> requestMap,String userRole) throws AllException {
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);

        Optional<Karyawan> deleteKaryawan = karyawanRepository.findBykodekaryawan(kodekaryawan);
        if (!deleteKaryawan.isPresent()) {
            throw new AllException("karyawan dengan kode karyawan" + kodekaryawan + "tidak ada");
        } else {

            karyawanRepository.deleteBykodekaryawan(kodekaryawan);
        }
    }


    public Karyawan updateKaryawanByKodeKaryawan(String kodekaryawan, Karyawan karyawan,Map<String,String> requestMap,String userRole) throws AllException {
        log.info("Received request with payload: {}", requestMap);

        userRole = requestMap.get("role");
        log.info("User Role from request: {}", userRole);

        if (karyawan.getNama() == null || karyawan.getNama().isEmpty()) {
            throw new AllException("Nama harus di isi !!!");
        }
        if (karyawan.getEmail() == null || karyawan.getEmail().isEmpty()) {
            throw new AllException("Email harus di isi !!!");
        }
        if (karyawan.getJabatan() == null || karyawan.getJabatan().isEmpty()) {
            throw new AllException("Jabatan harus di isi !!!");
        }
        if (karyawan.getPhone() == null || karyawan.getPhone().isEmpty()) {
            throw new AllException("No HP harus di isi !!!");
        }

        Optional<LocalDate> tgllahir = Optional.ofNullable(karyawan.getTgl_lahir());
        if (tgllahir.isPresent()) {
            String tanggalLahir = tgllahir.get().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (tanggalLahir.isEmpty()) {
                throw new AllException("Tanggal lahir harus di isi !!!");
            }
        } else {
            throw new AllException("Tanggal lahir harus di isi !!!");
        }

        Karyawan updatedKaryawan = karyawanRepository.findBykodekaryawan(kodekaryawan)
                .orElseThrow(() -> new AllException("karyawan dengan kode karyawan" + kodekaryawan + "tidak ada"));

        updatedKaryawan.setNama(karyawan.getNama());
        updatedKaryawan.setEmail(karyawan.getEmail());
        updatedKaryawan.setUmur(karyawan.getUmur());
        updatedKaryawan.setJabatan(karyawan.getJabatan());
        updatedKaryawan.setPhone(karyawan.getPhone());
        updatedKaryawan.setImageurl(karyawan.getImageurl());
        updatedKaryawan.setTgl_lahir(karyawan.getTgl_lahir());
        karyawanRepository.save(updatedKaryawan);

        return updatedKaryawan;
    }
}
