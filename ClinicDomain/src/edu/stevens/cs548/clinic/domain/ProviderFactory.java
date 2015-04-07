package edu.stevens.cs548.clinic.domain;

public class ProviderFactory implements IProviderFactory {

	@Override
	public Provider createProvider(long NPI, String name, String providerType) {
		Provider provider = new Provider(); 
		provider.setNPI(NPI);
		provider.setName(name);
		provider.setProviderType(providerType);
		return provider;
	}

}
