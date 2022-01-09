package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.Knjiga;
import model.Primerak;

@Repository
@Transactional
public class KnjigaRepoSpecificImpl implements KnjigaRepoSpecific{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<Integer> savePrimerci(Knjiga k, Integer brPrimeraka) {
		ArrayList<Integer> invBrojevi = new ArrayList<Integer>();
		try {
			if(k!=null) {
				for(int i=0;i<brPrimeraka;i++) {
					Primerak p = new Primerak();
					p.setKnjiga(k);
					em.persist(p);
					invBrojevi.add(p.getInvBroj());
				}
			}
			return invBrojevi;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
