package managers;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import model.Clan;
import model.Kategorija;
import model.Primerak;
import model.Zaduzenje;

public class ClanManager {
	public Clan saveClan(String ime, String prezime, String adresa, 
			Date datumRodjenja, Date datumUpisa, int idKategorije) {
		try {
			EntityManager em = JPAUtil.getEntityManager();
			em.getTransaction().begin();
			
			Kategorija k = em.find(Kategorija.class, idKategorije);
			Clan c = new Clan();
			c.setIme(ime);
			c.setPrezime(prezime);
			c.setAdresa(adresa);
			c.setDatumRodjenja(datumRodjenja);
			c.setDatumUpisa(datumUpisa);
			c.setKategorija(k);
			em.persist(c);
			
			em.getTransaction().commit();
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Kategorija> getSveKategorije(){
		EntityManager em = JPAUtil.getEntityManager();
		List<Kategorija> kat = em.createQuery("from Kategorija k", Kategorija.class).getResultList();
		return kat;
	}
	
	public Zaduzenje saveZaduzenje(int clBroj, int invBroj) {
		try {
			EntityManager em =JPAUtil.getEntityManager();
			em.getTransaction().begin();
			
			Clan c = em.find(Clan.class, clBroj);
			Primerak p = em.find(Primerak.class, invBroj);
			
			Zaduzenje z = new Zaduzenje();
			z.setClan(c);
			z.setPrimerak(p);
			z.setDatumZaduzenja(new Date());
			
			em.persist(z);
			
			em.getTransaction().commit();
			return z;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Zaduzenje> getSvaZaduzenja(int clBr){
		EntityManager em = JPAUtil.getEntityManager();
		List<Zaduzenje> zad = em.createQuery("select z from Zaduzenje z where z.clan.clanskibroj= :clBroj", Zaduzenje.class)
								.setParameter("clBroj", clBr).getResultList();
		return zad;
	}
	
	
	public boolean razduzi(int id) {
		try {
			EntityManager em = JPAUtil.getEntityManager();
			em.getTransaction().begin();
			
			Zaduzenje z = em.find(Zaduzenje.class, id);
			z.setDatumVracanja(new Date());
			em.merge(z);
			
			em.getTransaction().commit();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteClan(Integer clBroj) {
		try {
			EntityManager em=JPAUtil.getEntityManager();
			em.getTransaction().begin();
			
			Clan c = em.find(Clan.class, clBroj);
		
			List<Zaduzenje> zaduzenjaClana = em.createQuery(""
					+ "select z from Zaduzenje z where "
					+ "z.clan.clanskibroj=:br", Zaduzenje.class)
					.setParameter("br", clBroj)
					.getResultList();
			
			for(Zaduzenje z:zaduzenjaClana) {
				em.remove(z);
			}
			em.remove(c);
			em.getTransaction().commit();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Clan> clanovi(){
		EntityManager em=JPAUtil.getEntityManager();
		return em.createQuery("select c from Clan c inner join fetch c.zaduzenjes z", Clan.class).getResultList();
	}
	
	public static void main(String[] args) {
		ClanManager cm = new ClanManager();
		
		List<Clan> clanovi = cm.clanovi();
		System.out.println(clanovi.size());
		for(Clan c: clanovi) {
			System.out.println(c.getClanskibroj()+", "+c.getIme()+" "+c.getPrezime()+" "+c.getZaduzenjes().size());
		}
	}
}
