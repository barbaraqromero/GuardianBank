package br.com.zup.Guardians_Bank.proposta;

import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamento;
import br.com.zup.Guardians_Bank.infoPagamento.InfoPagamentoService;

import br.com.zup.Guardians_Bank.infoPagamento.dto.RetornoInfoDTO;
import br.com.zup.Guardians_Bank.proposta.dtos.OpcoesPagamentoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/propostas")
@Api(value = "API para compra de crédito")
@CrossOrigin(origins = "*")
public class PropostaController {

  @Autowired
  private PropostaService propostaService;
  @Autowired
  private InfoPagamentoService infoPagamentoService;
  @Autowired
  private ModelMapper modelMapper;


  @GetMapping("/{id}")
  @ApiOperation(value = "Método responsável por exibir as opções de parcelamento")
  public OpcoesPagamentoDTO exibirOpcoesPagamento(@PathVariable String id) {
    List<RetornoInfoDTO> retornoInfoDTOList = new ArrayList<>();
    OpcoesPagamentoDTO opcoesPagamentoDTO = new OpcoesPagamentoDTO();
    InfoPagamento info = propostaService.atribuirPropostaNoInfoPagamento(id);
    for (InfoPagamento infoPagamento : propostaService.exibirListaPagamento(info)) {
      RetornoInfoDTO retornoInfoDTO = modelMapper.map(infoPagamento, RetornoInfoDTO.class);
      retornoInfoDTO.setParcelaEspecial("INATIVO");
      if (retornoInfoDTO.getQtdadeParcelas() == 4) {
        retornoInfoDTO.setParcelaEspecial("ATIVO");
      }
      retornoInfoDTOList.add(retornoInfoDTO);
    }
    opcoesPagamentoDTO.setCodcli(info.getProposta().getCliente().getCodcli());
    opcoesPagamentoDTO.setNumeroDaProposta(info.getProposta().getNumeroProposta());
    opcoesPagamentoDTO.setOpcoes(retornoInfoDTOList);
    return opcoesPagamentoDTO;
  }

}
