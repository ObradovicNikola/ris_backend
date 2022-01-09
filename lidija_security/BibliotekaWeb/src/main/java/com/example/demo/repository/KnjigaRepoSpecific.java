package com.example.demo.repository;

import java.util.List;

import model.Knjiga;

public interface KnjigaRepoSpecific {

	public List<Integer> savePrimerci(Knjiga k, Integer brPrimeraka);
}
