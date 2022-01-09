package com.example.demo.converter;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import com.example.demo.repository.KategorijaRepository;

import model.Kategorija;

public class KategorijaKonverter implements Converter<String, Kategorija> {

	KategorijaRepository kr;
	
	 public KategorijaKonverter(KategorijaRepository kr) {
		this.kr = kr;
	}
	
	@Override
	public Kategorija convert(String source) {
		int katid=-1;
		try {
			katid = Integer.parseInt(source);
		}
		catch (NumberFormatException e) {
			throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), 
					TypeDescriptor.valueOf(Kategorija.class), katid, null);
		}
		Kategorija k = kr.findById(katid).get();
		return k;
	}

}