
import java.util.*;

class Transacao {
    String tipo;
    double valor;
    double saldoApos;
    Date dataHora;

    public Transacao(String tipo, double valor, double saldoApos) {
        this.tipo = tipo;
        this.valor = valor;
        this.saldoApos = saldoApos;
        this.dataHora = new Date();
    }

    public String toString() {
        return tipo + " de R$ " + valor + " | Saldo após: R$ " + saldoApos + " | Data: " + dataHora;
    }
}

abstract class Conta {
    String numero;
    String titular;
    double saldo;
    ArrayList<Transacao> historico = new ArrayList<>();

    public Conta(String numero, String titular, double saldoInicial) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public void depositar(double valor) {
        saldo += valor;
        historico.add(new Transacao("Depósito", valor, saldo));
    }

    public abstract void sacar(double valor);

    public void extrato() {
        for (Transacao t : historico) {
            System.out.println(t);
        }
    }
}

class ContaPoupanca extends Conta {
    public ContaPoupanca(String numero, String titular, double saldoInicial) {
        super(numero, titular, saldoInicial);
    }

    public void sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            historico.add(new Transacao("Saque", valor, saldo));
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }
}

class ContaCorrente extends Conta {
    double limite;

    public ContaCorrente(String numero, String titular, double saldoInicial, double limite) {
        super(numero, titular, saldoInicial);
        this.limite = limite;
    }

    public void sacar(double valor) {
        if (valor <= saldo + limite) {
            saldo -= valor;
            historico.add(new Transacao("Saque", valor, saldo));
        } else {
            System.out.println("Limite insuficiente.");
        }
    }
}

public class SistemaBancario {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Conta> contas = new ArrayList<>();

        while (true) {
            System.out.println("\n1-Criar Conta | 2-Depositar | 3-Sacar | 4-Extrato | 5-Sair");
            String opcao = sc.nextLine();

            if (opcao.equals("1")) {
                try {
                    System.out.println("Tipo (1-Corrente / 2-Poupança):");
                    String tipo = sc.nextLine();
                    System.out.println("Número da conta:");
                    String numero = sc.nextLine();

                    boolean existe = false;
                    for (Conta c : contas) {
                        if (c.numero.equals(numero)) {
                            existe = true;
                            break;
                        }
                    }

                    if (existe) {
                        System.out.println("Conta com esse número já existe.");
                        continue;
                    }

                    System.out.println("Nome do titular:");
                    String nome = sc.nextLine();
                    System.out.println("Saldo inicial:");
                    double saldo = Double.parseDouble(sc.nextLine());

                    if (tipo.equals("1")) {
                        System.out.println("Limite cheque especial:");
                        double limite = Double.parseDouble(sc.nextLine());
                        contas.add(new ContaCorrente(numero, nome, saldo, limite));
                    } else {
                        contas.add(new ContaPoupanca(numero, nome, saldo));
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao criar conta.");
                }

            } else if (opcao.equals("2")) {
                try {
                    System.out.println("Número da conta:");
                    String numero = sc.nextLine();
                    Conta conta = null;

                    for (Conta c : contas) {
                        if (c.numero.equals(numero)) {
                            conta = c;
                            break;
                        }
                    }

                    if (conta == null) {
                        System.out.println("Conta não encontrada.");
                        continue;
                    }

                    System.out.println("Valor do depósito:");
                    double valor = Double.parseDouble(sc.nextLine());
                    conta.depositar(valor);

                } catch (Exception e) {
                    System.out.println("Erro no depósito.");
                }

            } else if (opcao.equals("3")) {
                try {
                    System.out.println("Número da conta:");
                    String numero = sc.nextLine();
                    Conta conta = null;

                    for (Conta c : contas) {
                        if (c.numero.equals(numero)) {
                            conta = c;
                            break;
                        }
                    }

                    if (conta == null) {
                        System.out.println("Conta não encontrada.");
                        continue;
                    }

                    System.out.println("Valor do saque:");
                    double valor = Double.parseDouble(sc.nextLine());
                    conta.sacar(valor);

                } catch (Exception e) {
                    System.out.println("Erro no saque.");
                }

            } else if (opcao.equals("4")) {
                System.out.println("Número da conta:");
                String numero = sc.nextLine();
                Conta conta = null;

                for (Conta c : contas) {
                    if (c.numero.equals(numero)) {
                        conta = c;
                        break;
                    }
                }

                if (conta == null) {
                    System.out.println("Conta não encontrada.");
                    continue;
                }

                conta.extrato();

            } else if (opcao.equals("5")) {
                break;
            }
        }

        sc.close();
    }
}
