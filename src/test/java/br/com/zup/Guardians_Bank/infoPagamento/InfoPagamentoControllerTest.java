package br.com.zup.Guardians_Bank.infoPagamento;

import br.com.zup.Guardians_Bank.components.Conversor;
import br.com.zup.Guardians_Bank.infoPagamento.dto.AtualizarStatusDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.EntradaInfoDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.RespostaAtualizacaoStatusDTO;
import br.com.zup.Guardians_Bank.infoPagamento.dto.SaidaInfoDTO;
import br.com.zup.Guardians_Bank.proposta.Proposta;
import br.com.zup.Guardians_Bank.proposta.PropostaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({InfoPagamentoController.class, Conversor.class})
public class InfoPagamentoControllerTest {

  @MockBean
  private InfoPagamentoService infoPagamentoService;
  @MockBean
  private PropostaService propostaService;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;
  private InfoPagamento infoPagamento;
  private EntradaInfoDTO entradaInfoDTO;
  private SaidaInfoDTO saidaInfoDTO;
  private AtualizarStatusDTO atualizarStatusDTO;
  private RespostaAtualizacaoStatusDTO respostaAtualizacaoStatusDTO;
  private Proposta proposta;


  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
    proposta = new Proposta();

    infoPagamento = new InfoPagamento();
    infoPagamento.setIdPagamento("1");
    infoPagamento.setProposta(proposta);
    infoPagamento.setValorParcela(1200.00);
    infoPagamento.setQtdadeDeParcelas(4);
    infoPagamento.setImposto(0.25);

    entradaInfoDTO = new EntradaInfoDTO();
    entradaInfoDTO.setNumeroProposta("1");
    entradaInfoDTO.setQtdadeParcelas(8);

    saidaInfoDTO = new SaidaInfoDTO();
    atualizarStatusDTO = new AtualizarStatusDTO();
    respostaAtualizacaoStatusDTO = new RespostaAtualizacaoStatusDTO();

  }

  @Test
  public void testarCadastroInfoPagamento() throws Exception {
    Mockito.when(infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt())).thenReturn(infoPagamento);

    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect((MockMvcResultMatchers.status().is(201)));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    SaidaInfoDTO respostaInfo = objectMapper.readValue(jsonResposta, SaidaInfoDTO.class);
  }

  @Test
  public void testarValidacaoNumeroPropostaBlank() throws Exception {
    entradaInfoDTO.setNumeroProposta("    ");
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  public void testarValidacaoNumeroPropostaNotBlank() throws Exception {
    entradaInfoDTO.setNumeroProposta("1");
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  public void testarValidacaoQtdadeParcelasNull() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(null);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  public void testarValidacaoQtdadeParcelasNotNull() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(4);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  public void testarValidacaoQtdadeParcelasNumeroNegativo() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(-2);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(422));
  }

  @Test
  public void testarValidacaoQtdadeParcelasNumeroPositivo() throws Exception {
    entradaInfoDTO.setQtdadeParcelas(4);
    Mockito.when((infoPagamentoService.salvarInfoPagamento(Mockito.any(InfoPagamento.class), Mockito.anyString(),
        Mockito.anyInt()))).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(entradaInfoDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/infos")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(201));
  }

  @Test
  public void testarAtualizarStatusProposta() throws Exception {
    Mockito.when(infoPagamentoService.atualizarInfo(Mockito.anyString())).thenReturn(infoPagamento);
    String json = objectMapper.writeValueAsString(atualizarStatusDTO);

    ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/infos/1")
            .content(json).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200));

    String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
    RespostaAtualizacaoStatusDTO respostaAtualizada = objectMapper.readValue(jsonResposta,
        RespostaAtualizacaoStatusDTO.class);
  }

}