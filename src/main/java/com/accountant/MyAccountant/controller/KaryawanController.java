package com.accountant.MyAccountant.controller;

import com.accountant.MyAccountant.entity.Barang;
import com.accountant.MyAccountant.entity.Karyawan;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.service.KaryawanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/karyawan")
public class KaryawanController {
    @Autowired
    private KaryawanService karyawanService;

    @PostMapping("/add")
    public Karyawan addKaryawan(@RequestBody Karyawan karyawan, Map<String,String> requestMap) throws AllException {
        return karyawanService.addKaryawan(karyawan,requestMap);
    }
    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Karyawan> showAllKaryawan(){
        return karyawanService.showAllKaryawan();
    }

    @GetMapping("/sortasc/{field}")
    public List<Karyawan> listKaryawanByAsc(@PathVariable String field){
        return karyawanService.listKaryawanAscending(field);
    }

    @GetMapping("/sortdsc/{field}")
    public List<Karyawan> listKaryawanByDsc(@PathVariable String field){
        return karyawanService.listKaryawanDescending(field);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<List<Karyawan>> showAllKaryawanPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Karyawan> karyawanWithPagination = karyawanService.showAllKaryawanWithPagination(offset, pageSize);

        List<Karyawan> karyawanList = karyawanWithPagination.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(karyawanWithPagination.getTotalElements()));

        return new ResponseEntity<>(karyawanList, headers, HttpStatus.OK);
    }
    @GetMapping("/paginationwithascname/{offset}/{pageSize}")
    public ResponseEntity<List<Karyawan>> showAllKaryawanPaginationWithAscName(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Karyawan> karyawanWithPaginationAscName = karyawanService.showAllKaryawanWithPaginationAscName(offset, pageSize);

        List<Karyawan> karyawanList = karyawanWithPaginationAscName.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(karyawanWithPaginationAscName.getTotalElements()));

        return new ResponseEntity<>(karyawanList, headers, HttpStatus.OK);
    }
    @GetMapping("/paginationwithdescname/{offset}/{pageSize}")
    public ResponseEntity<List<Karyawan>> showAllKaryawanPaginationWithDescName(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Karyawan> karyawanWithPaginationDescName = karyawanService.showAllKaryawanWithPaginationDescName(offset, pageSize);

        List<Karyawan> karyawanList = karyawanWithPaginationDescName.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(karyawanWithPaginationDescName.getTotalElements()));

        return new ResponseEntity<>(karyawanList, headers, HttpStatus.OK);
    }

    @GetMapping("/cari/{id}")
    public Karyawan fetchKaryawanById(@PathVariable("id") Long id) throws AllException {
        return karyawanService.fetchKaryawanById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteKaryawanById(@PathVariable("id") Long id,Map<String,String> requestMap,String userRole) throws AllException {
        karyawanService.deleteKaryawanById(id,requestMap,userRole);
        return "Data karyawan telah dihapus!!";
    }

        @PutMapping("/update/{id}")
        public Karyawan updateKaryawan(@PathVariable("id") Long id,
                                       @RequestBody Karyawan karyawan,Map<String,String> requestMap,String userRole) throws AllException {
            return karyawanService.updateKaryawan(id,karyawan,requestMap,userRole);
        }

    @DeleteMapping("/deletecode/{kodekaryawan}")
    public String deleteKaryawanByCode(@PathVariable("kodekaryawan")String kodekaryawan,Map<String,String> requestMap,String userRole) throws AllException {
        karyawanService.deleteKaryawanByKodeKaryawan(kodekaryawan,requestMap,userRole);
        return "Data Karyawan telah dihapus!!";
    }

    @PutMapping("/updatecode/{kodekaryawan}")
    public Karyawan updateKaryawanByCode(@PathVariable("kodekaryawan")String kodekaryawan, @RequestBody Karyawan karyawan,Map<String,String> requestMap,String userRole ) throws AllException {
        return karyawanService.updateKaryawanByKodeKaryawan(kodekaryawan,karyawan,requestMap,userRole);
    }

}
