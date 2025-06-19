package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.adapters.BoletoParserGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BoletoParserRepository implements BoletoParserGateway {
    
    private static final Map<String, String> BANCOS = new HashMap<>();
    
    static {
        // Principais bancos brasileiros
        BANCOS.put("001", "BANCO DO BRASIL SA");
        BANCOS.put("033", "BANCO SANTANDER (BRASIL) SA");
        BANCOS.put("104", "CAIXA ECONOMICA FEDERAL");
        BANCOS.put("237", "BRADESCO SA");
        BANCOS.put("341", "BANCO ITAU SA");
        BANCOS.put("356", "BANCO REAL SA");
        BANCOS.put("399", "HSBC BANK BRASIL SA");
        BANCOS.put("422", "SAFRA SA");
        BANCOS.put("745", "CITIBANK NA");
        BANCOS.put("756", "BANCO COOPERATIVO DO BRASIL SA");
        BANCOS.put("085", "CECRED");
        BANCOS.put("097", "CREDISIS");
        BANCOS.put("136", "UNICRED BRASIL");
        BANCOS.put("212", "BANCO ORIGINAL");
        BANCOS.put("260", "NU PAGAMENTOS SA");
        BANCOS.put("336", "C6 BANK");
        BANCOS.put("380", "PICPAY");
        BANCOS.put("655", "BANCO VOTORANTIM SA");
        BANCOS.put("077", "BANCO INTER");
        BANCOS.put("218", "BANCO BS2 SA");
        BANCOS.put("318", "BANCO BMG SA");
        BANCOS.put("623", "BANCO PAN");
        BANCOS.put("633", "BANCO RENDIMENTO SA");
        BANCOS.put("652", "ITAÚ UNIBANCO HOLDING SA");
        BANCOS.put("246", "BANCO ABC BRASIL SA");
        BANCOS.put("025", "BANCO ALFA SA");
        BANCOS.put("641", "BANCO ALVORADA SA");
        BANCOS.put("029", "BANCO BANERJ SA");
        BANCOS.put("038", "BANCO BANESTADO SA");
        BANCOS.put("000", "BANCO BANCOPAULISTA SA");
        BANCOS.put("740", "BANCO BARCLAYS SA");
        BANCOS.put("107", "BANCO BBM SA");
        BANCOS.put("031", "BANCO BEG SA");
        BANCOS.put("096", "BANCO BM&F DE SERVICOS DE LIQUIDACAO E CUSTODIA SA");
        BANCOS.put("318", "BANCO BMG SA");
        BANCOS.put("752", "BANCO BNP PARIBAS BRASIL SA");
        BANCOS.put("248", "BANCO BOAVISTA INTERATLANTICO SA");
        BANCOS.put("036", "BANCO BRADESCO BBI SA");
        BANCOS.put("204", "BANCO BRADESCO CARTÕES SA");
        BANCOS.put("225", "BANCO BRASCAN SA");
        BANCOS.put("044", "BANCO BVA SA");
        BANCOS.put("263", "BANCO CACIQUE SA");
        BANCOS.put("473", "BANCO CAIXA GERAL BRASIL SA");
        BANCOS.put("040", "BANCO CARGILL SA");
        BANCOS.put("233", "BANCO CIFRA SA");
        BANCOS.put("745", "BANCO CITIBANK SA");
        BANCOS.put("M03", "BANCO DA AMAZONIA SA");
        BANCOS.put("M07", "BANCO DO NORDESTE DO BRASIL SA");
        BANCOS.put("M06", "BANCO DO ESTADO DE SERGIPE SA");
        BANCOS.put("M31", "BANCO DO ESTADO DO PARA SA");
        BANCOS.put("M37", "BANCO DO ESTADO DO RIO GRANDE DO SUL SA");
        BANCOS.put("M20", "BANCO DE CREDITO NACIONAL SA");
        BANCOS.put("M22", "BANCO DE CREDITO REAL DE MINAS GERAIS SA");
        BANCOS.put("M36", "BANCO DO ESTADO DO ACRE SA");
        BANCOS.put("M21", "BANCO DO ESTADO DO ESPIRITO SANTO SA");
        BANCOS.put("M11", "BANCO DO ESTADO DO RIO DE JANEIRO SA");
        BANCOS.put("M28", "BANCO DO ESTADO DE SANTA CATARINA SA");
        BANCOS.put("M43", "BANCO DO ESTADO DO PIAUI SA");
        BANCOS.put("M23", "BANCO DO ESTADO DE SAO PAULO SA");
        BANCOS.put("M03", "BANCO DO ESTADO DE MATO GROSSO SA");
        BANCOS.put("M08", "BANCO DO ESTADO DE MATO GROSSO DO SUL SA");
        BANCOS.put("M35", "BANCO DO ESTADO DO CEARA SA");
        BANCOS.put("M24", "BANCO DO ESTADO DE ALAGOAS SA");
        BANCOS.put("M25", "BANCO DO ESTADO DE PERNAMBUCO SA");
        BANCOS.put("M26", "BANCO DO ESTADO DE GOIAS SA");
        BANCOS.put("M27", "BANCO DO ESTADO DE MINAS GERAIS SA");
        BANCOS.put("M29", "BANCO DO ESTADO DA BAHIA SA");
        BANCOS.put("M30", "BANCO DO ESTADO DA PARAIBA SA");
        BANCOS.put("M32", "BANCO DO ESTADO DE ALAGOAS SA");
        BANCOS.put("M33", "BANCO DO ESTADO DE SANTA CATARINA SA");
        BANCOS.put("M34", "BANCO DO ESTADO DE PERNAMBUCO SA");
        BANCOS.put("M38", "BANCO DO ESTADO DO PARANA SA");
        BANCOS.put("M39", "BANCO DO ESTADO DO PIAUI SA");
        BANCOS.put("M40", "BANCO DO ESTADO DO CEARA SA");
        BANCOS.put("M41", "BANCO DO ESTADO DO RIO GRANDE DO NORTE SA");
        BANCOS.put("M42", "BANCO DO ESTADO DE SERGIPE SA");
        BANCOS.put("M44", "BANCO DO ESTADO DE RONDONIA SA");
        BANCOS.put("M45", "BANCO DO ESTADO DE RORAIMA SA");
        BANCOS.put("M46", "BANCO DO ESTADO DO AMAPA SA");
        BANCOS.put("M47", "BANCO DO ESTADO DO TOCANTINS SA");
        BANCOS.put("M48", "BANCO DO ESTADO DO DISTRITO FEDERAL SA");
        BANCOS.put("M49", "BANCO DO ESTADO DE GOIAS SA");
        BANCOS.put("M50", "BANCO DO ESTADO DE MINAS GERAIS SA");
        BANCOS.put("M51", "BANCO DO ESTADO DO ESPIRITO SANTO SA");
        BANCOS.put("M52", "BANCO DO ESTADO DO RIO DE JANEIRO SA");
        BANCOS.put("M53", "BANCO DO ESTADO DE SAO PAULO SA");
        BANCOS.put("M54", "BANCO DO ESTADO DE SANTA CATARINA SA");
        BANCOS.put("M55", "BANCO DO ESTADO DO RIO GRANDE DO SUL SA");
        BANCOS.put("M56", "BANCO DO ESTADO DO PARANA SA");
        BANCOS.put("M57", "BANCO DO ESTADO DE PERNAMBUCO SA");
        BANCOS.put("M58", "BANCO DO ESTADO DA BAHIA SA");
        BANCOS.put("M59", "BANCO DO ESTADO DA PARAIBA SA");
        BANCOS.put("M60", "BANCO DO ESTADO DE ALAGOAS SA");
        BANCOS.put("M61", "BANCO DO ESTADO DE SERGIPE SA");
        BANCOS.put("M62", "BANCO DO ESTADO DO PIAUI SA");
        BANCOS.put("M63", "BANCO DO ESTADO DO CEARA SA");
        BANCOS.put("M64", "BANCO DO ESTADO DO RIO GRANDE DO NORTE SA");
        BANCOS.put("M65", "BANCO DO ESTADO DE RONDONIA SA");
        BANCOS.put("M66", "BANCO DO ESTADO DE RORAIMA SA");
        BANCOS.put("M67", "BANCO DO ESTADO DO AMAPA SA");
        BANCOS.put("M68", "BANCO DO ESTADO DO TOCANTINS SA");
        BANCOS.put("M69", "BANCO DO ESTADO DO DISTRITO FEDERAL SA");
        BANCOS.put("M70", "BANCO DO ESTADO DE GOIAS SA");
        BANCOS.put("M71", "BANCO DO ESTADO DE MINAS GERAIS SA");
        BANCOS.put("M72", "BANCO DO ESTADO DO ESPIRITO SANTO SA");
        BANCOS.put("M73", "BANCO DO ESTADO DO RIO DE JANEIRO SA");
        BANCOS.put("M74", "BANCO DO ESTADO DE SAO PAULO SA");
        BANCOS.put("M75", "BANCO DO ESTADO DE SANTA CATARINA SA");
        BANCOS.put("M76", "BANCO DO ESTADO DO RIO GRANDE DO SUL SA");
        BANCOS.put("M77", "BANCO DO ESTADO DO PARANA SA");
        BANCOS.put("M78", "BANCO DO ESTADO DE PERNAMBUCO SA");
        BANCOS.put("M79", "BANCO DO ESTADO DA BAHIA SA");
        BANCOS.put("M80", "BANCO DO ESTADO DA PARAIBA SA");
        BANCOS.put("M81", "BANCO DO ESTADO DE ALAGOAS SA");
        BANCOS.put("M82", "BANCO DO ESTADO DE SERGIPE SA");
        BANCOS.put("M83", "BANCO DO ESTADO DO PIAUI SA");
        BANCOS.put("M84", "BANCO DO ESTADO DO CEARA SA");
        BANCOS.put("M85", "BANCO DO ESTADO DO RIO GRANDE DO NORTE SA");
        BANCOS.put("M86", "BANCO DO ESTADO DE RONDONIA SA");
        BANCOS.put("M87", "BANCO DO ESTADO DE RORAIMA SA");
        BANCOS.put("M88", "BANCO DO ESTADO DO AMAPA SA");
        BANCOS.put("M89", "BANCO DO ESTADO DO TOCANTINS SA");
        BANCOS.put("M90", "BANCO DO ESTADO DO DISTRITO FEDERAL SA");
        BANCOS.put("M91", "BANCO DO ESTADO DE GOIAS SA");
        BANCOS.put("M92", "BANCO DO ESTADO DE MINAS GERAIS SA");
        BANCOS.put("M93", "BANCO DO ESTADO DO ESPIRITO SANTO SA");
        BANCOS.put("M94", "BANCO DO ESTADO DO RIO DE JANEIRO SA");
        BANCOS.put("M95", "BANCO DO ESTADO DE SAO PAULO SA");
        BANCOS.put("M96", "BANCO DO ESTADO DE SANTA CATARINA SA");
        BANCOS.put("M97", "BANCO DO ESTADO DO RIO GRANDE DO SUL SA");
        BANCOS.put("M98", "BANCO DO ESTADO DO PARANA SA");
        BANCOS.put("M99", "BANCO DO ESTADO DE PERNAMBUCO SA");
    }
    
    @Override
    public Boleto parse(String linhaDigitavel) {
        if (linhaDigitavel == null || linhaDigitavel.length() != 47) {
            throw new IllegalArgumentException("Linha digitável deve ter exatamente 47 dígitos");
        }
        
        // Extrair informações básicas
        String codigoBanco = linhaDigitavel.substring(0, 3);
        String moeda = linhaDigitavel.substring(3, 4);
        String nomeBeneficiario = BANCOS.getOrDefault(codigoBanco, "BANCO NÃO IDENTIFICADO");
        
        return Boleto.builder()
                .linhaDigitavel(linhaDigitavel)
                .nomeBeneficiario(nomeBeneficiario)
                .bancoEmissor(codigoBanco)
                .codigoDoBanco(codigoBanco)
                .status(StatusValidacao.VALIDO)
                .build();
    }
} 