package managers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import model.Knjiga;
import model.Primerak;

public class KnjigaManager {
	
	public Knjiga saveKnjiga(String naslov, String autor, String izdavac, String godinaIzdanja) {
		try {
			EntityManager em = JPAUtil.getEntityManager();
			
			Knjiga k = new Knjiga();
			k.setAutor(autor);
			k.setGodinaIzdanja(godinaIzdanja);
			k.setIzdavac(izdavac);
			k.setNaslov(naslov);
			
			em.getTransaction().begin();
			em.persist(k);
			em.getTransaction().commit();
			return k;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Knjiga> getSveKnjige(){
		EntityManager em = JPAUtil.getEntityManager();
		List<Knjiga> knjige = em.createQuery("from Knjiga", Knjiga.class).getResultList();
		return knjige;
	}
	
	public List<Integer> dodajPrimerke(int idKnjige, int brojPrimeraka){
		ArrayList<Integer> invBrojevi = new ArrayList<Integer>();
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Knjiga k = em.find(Knjiga.class, idKnjige);
			if(k!=null) {
				em.getTransaction().begin();
				for(int i=0;i<brojPrimeraka;i++) {
					Primerak p = new Primerak();
					p.setKnjiga(k);
					em.persist(p);
					invBrojevi.add(p.getInvBroj());
				}
				em.getTransaction().commit();
			}
			return invBrojevi;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		KnjigaManager km = new KnjigaManager();
//		Knjiga knjiga = km.saveKnjiga("Hajdi", "Johana Spiri", "Laguna", "1994");
//		if(knjiga!=null)
//			System.out.println("Knjiga je uspesno sacuvana. Id knjige je: "+knjiga.getIdKnjige());
		
		List<Knjiga> knjige = km.getSveKnjige();
		if(knjige!=null)
			for(Knjiga k:knjige)
				System.out.println(k.getNaslov()+", "+k.getAutor());

	}

}