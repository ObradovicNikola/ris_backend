package com.example.demo.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.repository.ClanRepository;
import com.example.demo.repository.KategorijaRepository;

import model.Clan;
import model.Kategorija;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping(value="/clanovi")
public class ClanKontroler {

	@Autowired
	KategorijaRepository kr;
	
	@Autowired
	ClanRepository cr;
	
	@RequestMapping(value="/getKategorije", method=RequestMethod.GET)
	public String getKategorije(HttpServletRequest request) {
		List<Kategorija> kat = kr.findAll();
		request.getSession().setAttribute("kategorije", kat);
		return "unos/UnosClana";
	}
	
	@RequestMapping(value="/saveClan", method=RequestMethod.POST)
	public String saveClan(String ime, String prezime, String adresa, Date datumRodjenja,
								Date datumUpisa, Integer idKat, Model m) {
		Clan c = new Clan();
		c.setAdresa(adresa);
		c.setDatumRodjenja(datumRodjenja);
		c.setDatumUpisa(datumUpisa);
		c.setIme(ime);
		c.setPrezime(prezime);
		
		Kategorija k = kr.findById(idKat).get();
		c.setKategorija(k);
		Clan clan = cr.save(c);
		
		m.addAttribute("clan", clan);
		
		return "unos/UnosClana";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
	
	@ModelAttribute("clan")
	public Clan getClan() {
		return new Clan();
	}
	
	@ModelAttribute("kategorije")
	public List<Kategorija> getKategorije(){
		return kr.findAll();
	}
	
	@RequestMapping(value="/unosClana", method=RequestMethod.GET)
	public String unosClana() {
		return "unos/UnosClana1";
	}
	
	@RequestMapping(value="/saveClan1", method=RequestMethod.POST)
	public String saveClan(@ModelAttribute("clan") Clan clan, Model m) {
		Clan cl = cr.save(clan);
		m.addAttribute("poruka", "Clan je uspesno sacuvan. Id clana je ");
		m.addAttribute("clanSaved", cl);
		return "unos/UnosClana1";
	}
	
	@RequestMapping(value="/getSviClanoviReport.pdf", method=RequestMethod.GET)
	public void showReport(HttpServletResponse response) throws Exception{
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cr.findAllByOrderByDatumUpisa());
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/SviClanoviReport.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imeBiblioteke", "Biblioteka DMI");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=SviClanovi.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
	
	@RequestMapping(value="/ClanoviUPeriodu.pdf", method=RequestMethod.GET)
	public void showReport1(HttpServletResponse response, Date datumOd, Date datumDo) throws Exception{
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cr.getClanoviUPeriodu(datumOd, datumDo));
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/ClanoviUPeriodu.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("datumOd", datumOd);
		params.put("datumDo", datumDo);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=ClanoviUPeriodu.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
}