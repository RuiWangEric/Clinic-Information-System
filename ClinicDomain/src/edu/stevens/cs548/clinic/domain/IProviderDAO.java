package edu.stevens.cs548.clinic.domain;

import java.util.List;

public interface IProviderDAO {
	
	public static class ProviderExn  extends Exception {
		private static final long serialVersionUID = 1L;	
		public ProviderExn (String msg) {
			super(msg);
		}
	}
	
	public Provider getProviderByDbId (long id) throws ProviderExn;
	
	public Provider getProviderByNPI (long NPI) throws ProviderExn;
	
	public List<Provider> getProviderByName(String name) throws ProviderExn;
	
	public List<Provider> getProviderByNameProviderType (String name, String providerType) ;
	
	public long addProvider (Provider provider) throws ProviderExn;
	
	public void deleteProvider (Provider provider) throws ProviderExn;
	
	public int deleteAll() throws ProviderExn;

}
