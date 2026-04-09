import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface IServico extends Remote {
    public String hello() throws RemoteException; //exemplo

    public double embarcar(String descricao) throws RemoteException;
    public String relatorio_embarque() throws RemoteException;

    public Integer cadastrar_navio(String descricao, Integer capacidade) throws RemoteException;
    public void remover_navio(Integer id) throws RemoteException;
    public String relatorio_navio() throws RemoteException;

    public Integer cadastrar_carga(String descricao, Integer volume) throws RemoteException;
    public void remover_carga(Integer id) throws RemoteException;
    public String relatorio_carga() throws RemoteException;
}
