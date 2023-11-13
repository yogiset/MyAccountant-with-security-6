package com.accountant.MyAccountant.controller;

import com.accountant.MyAccountant.entity.Barang;
import com.accountant.MyAccountant.entity.User;
import com.accountant.MyAccountant.exception.AllException;
import com.accountant.MyAccountant.service.BarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    public Barang addBarang(@RequestBody Barang barang,Map<String,String> requestMap,String userRole,User user) throws AllException {
        return barangService.addBarang(barang,requestMap,userRole,user);
    }
    @GetMapping("/all")
    public List<Barang> listBarang(){
        return barangService.listBarang();
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
