package porto;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://porto/", name = "IServico", serviceName = "WSPorto")
public interface IServico {

    @WebMethod
    String hello();

    @WebMethod
    double embarcar(String descricao);

    @WebMethod
    String relatorio_embarque();

    @WebMethod
    Integer cadastrar_navio(String descricao, Integer capacidade);

    @WebMethod
    void remover_navio(Integer id);

    @WebMethod
    String relatorio_navio();

    @WebMethod
    Integer cadastrar_carga(String descricao, Integer volume);

    @WebMethod
    void remover_carga(Integer id);

    @WebMethod
    String relatorio_carga();
}
