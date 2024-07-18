package com.healthPharmacy.Administracao;

import com.healthPharmacy.Entidade.Cliente;

import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class RemoverCliente {
  public static void removerCliente() throws InterruptedException{
    Scanner sc = new Scanner(System.in);
    System.out.println("Desligamento de cliente\n");
    System.out.println("Digite o CPF do cliente que deseja remover: ");
    System.out.print("--> ");

    String CPF = sc.nextLine();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    Cliente cliente = em.find(Cliente.class, CPF);
    if (cliente != null) {
      em.createQuery("DELETE FROM ItemPedido i WHERE i.pedido.id IN (SELECT p.id FROM PedidoFinalizado p WHERE p.cliente = :cliente)")
              .setParameter("cliente", cliente)
              .executeUpdate();

      // Remove todos os pedidos do cliente
      em.createQuery("DELETE FROM PedidoFinalizado p WHERE p.cliente = :cliente")
              .setParameter("cliente", cliente)
              .executeUpdate();


      cliente = em.merge(cliente);
      em.remove(cliente);
      em.getTransaction().commit();
      System.out.println("Cliente "+ cliente.getNome() +" removido com sucesso!");
      Thread.sleep(2000);
      MenuAdm.menuADM();
    } else {
      System.out.println("Cliente não encontrado!");
      Thread.sleep(2000);
      MenuAdm.menuADM();
    }
  }
}
