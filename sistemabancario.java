package sistemabancario;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaBancario {
    static class Usuario {
        private String nome;
        private String cpf;
        public Usuario(String nome, String cpf) {
            this.nome = nome;
            this.cpf = cpf;
        }
        public String getNome() { return nome; }
        public String getCpf() { return cpf; }
    }

    static class Conta {
        private String numero;
        private double saldo;
        public Conta(String numero, double saldoInicial) {
            this.numero = numero;
            this.saldo = saldoInicial;
        }
        public String getNumero() { return numero; }
        public double getSaldo() { return saldo; }
        public void depositar(double valor) { saldo += valor; }
        public boolean sacar(double valor) {
            if (valor <= saldo) { saldo -= valor; return true; }
            return false;
        }
    }

    static class Transacao {
        private String tipo;
        private double valor;
        private String dataHora;
        public Transacao(String tipo, double valor) {
            this.tipo = tipo;
            this.valor = valor;
            this.dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        public String toString() {
            return tipo + " - R$ " + valor + " - " + dataHora;
        }
    }

    private static Usuario usuario;
    private static Conta conta;
    private static List<Transacao> transacoes = new ArrayList<>();

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        Scanner sc = new Scanner(System.in);
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        usuario = new Usuario(nome, cpf);
        System.out.print("Número da conta: ");
        String numeroConta = sc.nextLine();
        conta = new Conta(numeroConta, 0);
        carregarDados();
        while (true) {
            System.out.println("1- Depositar  2- Sacar  3- Extrato  4- Sair");
            int op = sc.nextInt();
            if (op == 1) {
                System.out.print("Valor: ");
                double v = sc.nextDouble();
                conta.depositar(v);
                transacoes.add(new Transacao("Depósito", v));
                salvarDados();
            } else if (op == 2) {
                System.out.print("Valor: ");
                double v = sc.nextDouble();
                if (conta.sacar(v)) {
                    transacoes.add(new Transacao("Saque", v));
                    salvarDados();
                } else {
                    System.out.println("Saldo insuficiente");
                }
            } else if (op == 3) {
                System.out.println("Usuário: " + usuario.getNome());
                System.out.println("Conta: " + conta.getNumero());
                List<Transacao> copia = new ArrayList<>(transacoes);
                Collections.reverse(copia);
                for (Transacao t : copia) System.out.println(t);
                System.out.println("Saldo atual: R$ " + conta.getSaldo());
            } else {
                break;
            }
        }
        sc.close();
    }

    private static void salvarDados() {
        try {
            FileWriter fw = new FileWriter("usuario_" + usuario.getCpf() + ".txt");
            fw.write(usuario.getNome() + ";" + usuario.getCpf());
            fw.close();
            fw = new FileWriter("conta_" + usuario.getCpf() + ".txt");
            fw.write(conta.getNumero() + ";" + conta.getSaldo());
            fw.close();
            fw = new FileWriter("transacoes_" + usuario.getCpf() + ".txt");
            for (Transacao t : transacoes) fw.write(t.toString() + "\n");
            fw.close();
        } catch (IOException e) {}
    }

    private static void carregarDados() {
        try {
            File f = new File("conta_" + usuario.getCpf() + ".txt");
            if (f.exists()) {
                Scanner s = new Scanner(f);
                String[] dados = s.nextLine().split(";");
                conta = new Conta(dados[0], Double.parseDouble(dados[1]));
                s.close();
            }
            File ft = new File("transacoes_" + usuario.getCpf() + ".txt");
            if (ft.exists()) {
                Scanner s = new Scanner(ft);
                while (s.hasNextLine()) {
                    String linha = s.nextLine();
                    String[] p = linha.split(" - R\\$ ");
                    String tipo = p[0];
                    double valor = Double.parseDouble(p[1].split(" - ")[0]);
                    transacoes.add(new Transacao(tipo, valor));
                }
                s.close();
            }
        } catch (Exception e) {}
    }
}
