package com.accountant.MyAccountant.controller;

import com.accountant.MyAccountant.constant.ApiConstant;
import com.accountant.MyAccountant.entity.Barang;
import com.accountant.MyAccountant.entity.User;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.service.BarangService;
import com.accountant.MyAccountant.utils.UserUtils;
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
@RequestMapping("/barang")
public class BarangController {
    @Autowired
    private BarangService barangService;

    @PostMapping("/add")
    public Barang addBarang(@RequestBody Barang barang,Map<String,String> requestMap,String userRole) throws AllException {
        return barangService.addBarang(barang,requestMap,userRole);
    }

    @GetMapping("/all")
    public List<Barang> listBarang(){
        return barangService.listBarang();
    }

    @GetMapping("/sortasc/{field}")
    public List<Barang> listBarangByAsc(@PathVariable String field){
        return barangService.listBarangAscending(field);
    }

    @GetMapping("/sortdsc/{field}")
    public List<Barang> listBarangByDsc(@PathVariable String field){
        return barangService.listBarangDescending(field);
    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<List<Barang>> showAllBarangPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Barang> barangWithPagination = barangService.showAllBarangWithPagination(offset, pageSize);

        List<Barang> barangList = barangWithPagination.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(barangWithPagination.getTotalElements()));

        return new ResponseEntity<>(barangList, headers, HttpStatus.OK);
    }
    @GetMapping("/paginationwithascname/{offset}/{pageSize}")
    public ResponseEntity<List<Barang>> showAllBarangPaginationWithAscName(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Barang> barangWithPaginationAscName = barangService.showAllBarangWithPaginationAscName(offset, pageSize);

        List<Barang> barangList = barangWithPaginationAscName.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(barangWithPaginationAscName.getTotalElements()));

        return new ResponseEntity<>(barangList, headers, HttpStatus.OK);
    }
    @GetMapping("/paginationwithdescname/{offset}/{pageSize}")
    public ResponseEntity<List<Barang>> showAllBarangPaginationWithDescName(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Barang> barangWithPaginationDescName = barangService.showAllBarangWithPaginationDescName(offset, pageSize);

        List<Barang> barangList = barangWithPaginationDescName.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(barangWithPaginationDescName.getTotalElements()));

        return new ResponseEntity<>(barangList, headers, HttpStatus.OK);
    }

    @GetMapping("/cari/{id}")
    public Barang listBarangById(@PathVariable("id")Long id) throws AllException {
        return barangService.listBarangById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBarangById(@PathVariable("id")Long id,Map<String,String> requestMap,String userRole) throws AllException {
         barangService.deleteBarangById(id,requestMap,userRole);
     return "Data Barang telah dihapus!!";
    }

    @PutMapping("/update/{id}")
    public Barang updateBarangById(@PathVariable("id")Long id,@RequestBody Barang barang,Map<String,String> requestMap,String userRole ) throws AllException {
        return barangService.updateBarangById(id,barang,requestMap,userRole);
    }

    @DeleteMapping("/deletecode/{kodebarang}")
    public String deleteBarangByCode(@PathVariable("kodebarang")String kodebarang,Map<String,String> requestMap,String userRole) throws AllException {
        barangService.deleteBarangByKodeBarang(kodebarang,requestMap,userRole);
        return "Data Barang telah dihapus!!";
    }


    @PutMapping("/updatecode/{kodebarang}")
    public Barang updateBarangByCode(@PathVariable("kodebarang")String kodebarang,@RequestBody Barang barang,Map<String,String> requestMap,String userRole ) throws AllException {
        return barangService.updateBarangByKodeBarang(kodebarang,barang,requestMap,userRole);
    }


}
