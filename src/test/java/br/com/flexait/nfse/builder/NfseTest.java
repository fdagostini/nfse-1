package br.com.flexait.nfse.builder;

import static br.com.flexait.nfse.builder.Nfse.contato;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.flexait.nfse.model.ExigibilidadeISS;
import br.com.flexait.nfse.model.LoteRps;
import br.com.flexait.nfse.model.Rps;

public class NfseTest {

	Nfse builder;

	@Before
	public void setUp() throws Exception {
		builder = Nfse.nfse();
	}

	@Test
	public void shouldReturnNfseBuilder() {
		assertThat(builder, notNullValue());
	}

	@Test
	public void shouldReturnXml() throws Exception {
		builder = builder.withLoteRps(new LoteNfseBuilder().build())
				.disableValidation();
		assertThat(builder.asXML(), containsString("<?xml"));
		assertThat(builder.asXML(),
				containsString("<EnviarLoteRpsEnvio xmlns="));
		assertThat(builder.asXML(), containsString("<LoteRps Id="));
	}

	@Test
	public void shouldReturnRpsBuilder() {
		assertThat(Nfse.rps(), instanceOf(RpsBuilder.class));
	}

	@Test
	public void shouldReturnAXmlEnviarLote() throws Exception {
		String xml = builder.disableValidation().asXML();
		assertThat(xml, containsString("<EnviarLoteRpsEnvio"));
	}

	@Test
	public void shouldAddXmlHeader() throws Exception {
		String xml = builder.disableValidation().asXML();
		assertThat(xml, containsString("<?xml"));
	}

	@Test
	public void shouldAddLoteRps() throws Exception {
		LoteRps lote = Nfse.loteNfse().withCnpj("123").build();
		Nfse nfse = builder.disableValidation().withLoteRps(lote);

		assertThat(nfse.asXML(), containsString("<Cnpj>123</Cnpj>"));
	}

	@Test
	public void shouldAddRps() throws Exception {
		LoteRps lote = Nfse.loteNfse().withCnpj("123")
				.addRps(Nfse.rps().cancelado().build()).build();
		Nfse nfse = builder.withLoteRps(lote).disableValidation();

		assertThat(nfse.asXML(), containsString("<Status>2</Status>"));
		assertThat(nfse.asXML(),
				containsString("<QuantidadeRps>1</QuantidadeRps>"));
	}

	@Test(expected = Exception.class)
	public void xmlShouldBeInvalid() throws Exception {
		Nfse.nfse().asXML();
	}

	@Test
	public void shouldDisableValidation() throws Exception {
		Nfse.nfse().disableValidation().asXML();
	}

	@Test
	public void shouldCreateAValidXml() throws Exception {
		Rps rps = Nfse.rps().withNumero(1L).withInfId("d")
		.withServico(
				Nfse.servico()
				.withValorServicos(10.01657987)
				.withItemListaServico("1")
				.withExigibilidadeISS(ExigibilidadeISS.EXIGIBILIDADE_SUSPENSA_PROCESSO_ADMINISTRATIVO)
				.withCodigoMunicipio(123)
				.withDiscriminacao("Test")
				.build()
		)
		.withPrestador(
				Nfse.prestador()
				.withCnpj("12312312312312")
				.build()
		)
		.withTomador(
				Nfse.tomador()
				.withCpf("00000000000")
				.withEndereco(
						Nfse.endereco()
						.withEndereco("Rua")
						.withNumero(1)
						.withBairro("Bairro")
						.withCodigoMunicipio(321)
						.withUf("ES")
						.withCep("29111111")
						.build()
				)
				.build()
		)
		.build();
		LoteRps lote = Nfse
				.loteNfse()
				.withCnpj("00000000000000")
				.withNumeroLote(123123L)
				.addRps(rps, rps).build();
		
		builder.withLoteRps(lote).asXML();
	}

	@Test
	public void shouldReturnServicoBuilder() {
		assertThat(Nfse.servico(), instanceOf(ServicoBuilder.class));
	}
	
	@Test
	public void shouldReturnPrestadorBuilder() {
		assertThat(Nfse.prestador(), instanceOf(PrestadorBuilder.class));
	}
	
	@Test
	public void shouldReturnTomadorBuilder() {
		assertThat(Nfse.tomador(), instanceOf(TomadorBuilder.class));
	}
	
	@Test
	public void shouldReturnEnderecoBuilder() {
		assertThat(Nfse.endereco(), instanceOf(EnderecoBuilder.class));
	}
	
	@Test
	public void shouldSetValorServicos() throws Exception {
		String xml = generateXML();
				assertThat(xml, containsString("<ValorServicos>214.41</ValorServicos>"));
	}
	
	private String generateXML() throws Exception {
		Rps rps = Nfse.rps().withNumero(1L).withInfId("d")
				.withServico(
						Nfse.servico()
						.withValorServicos(214.41)
						.withItemListaServico("1")
						.withExigibilidadeISS(ExigibilidadeISS.EXIGIBILIDADE_SUSPENSA_PROCESSO_ADMINISTRATIVO)
						.withCodigoMunicipio(123)
						.withDiscriminacao("Test")
						.build()
				)
				.withPrestador(
						Nfse.prestador()
						.withCnpj("12312312312312")
						.build()
				)
				.withTomador(
						Nfse.tomador()
						.withCpf("00000000000")
						.withEndereco(
								Nfse.endereco()
								.withEndereco("Rua")
								.withNumero(1)
								.withBairro("Bairro")
								.withComplemento("")
								.withCodigoMunicipio(321)
								.withUf("ES")
								.withCep("29111111")
								.build()
						)
						.withContato(
							contato()
								.withEmail("email@email.com")
								.withTelefone("2799999999")
								.build()
						)
						.build()
				)
				.build();
				LoteRps lote = Nfse
						.loteNfse()
						.withCnpj("00000000000000")
						.withNumeroLote(123123L)
						.addRps(rps, rps).build();
				
				String xml = builder.withLoteRps(lote).asXML();
		return xml;
	}
	
}