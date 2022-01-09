package com.example.demo.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.example.demo.repository.ClanRepository;

@Controller 
@RequestMapping(value="/reports") 
public class ReportsController {

	@Autowired
	ClanRepository cr;
	
	@RequestMapping(value="/SviClanovi.pdf", method=RequestMethod.GET)
	public void showReport(HttpServletResponse response) throws Exception{
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cr.findAll());
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/rpt_sviClanoviBiblioteke.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imeBiblioteke", "Biblioteka DMI");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();

	
		
//		HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//		exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//		exporter.exportReport();
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=SviClanovi.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
	
	@RequestMapping(value="/ClanoviUPeriodu.pdf", method=RequestMethod.GET)
	public void showReport1(HttpServletResponse response, Date datumOd, Date datumDo) throws Exception{
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cr.getClanoviUPeriodu(datumOd, datumDo));
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/rpt_ClanoviUPeriodu.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("datumOd", datumOd);
		params.put("datumDo", datumDo);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		
//		HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//		exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//		exporter.exportReport();
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=ClanoviUPeriodu.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    sdf.setLenient(true);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}
