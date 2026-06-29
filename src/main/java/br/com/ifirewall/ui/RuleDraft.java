package br.com.ifirewall.ui;

import br.com.ifirewall.core.model.Action;
import br.com.ifirewall.core.model.Chain;
import br.com.ifirewall.core.model.FirewallRule;
import br.com.ifirewall.core.model.IPAddress;
import br.com.ifirewall.core.model.Port;
import br.com.ifirewall.core.model.Protocol;

/**
 * Regra em construção no canvas: cada bloco solto preenche um "slot".
 *
 * <p>Concentra as regras de conexão lógica entre blocos (uma regra precisa de
 * Chain e Action; uma porta exige TCP/UDP) para que a UI dê feedback claro.</p>
 */
public class RuleDraft {

    private Chain chain;
    private Protocol protocol;
    private IPAddress sourceIp;
    private Port port;
    private Action action;

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setSourceIp(IPAddress sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Chain getChain() {
        return chain;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Action getAction() {
        return action;
    }

    /** Uma regra só pode ser adicionada quando tem, no mínimo, Chain e Action. */
    public boolean isComplete() {
        return chain != null && action != null;
    }

    /**
     * Constrói a regra de domínio. Lança {@link IllegalStateException} se faltar
     * Chain/Action e propaga a validação de integridade de {@link FirewallRule}
     * (ex.: porta sem TCP/UDP).
     */
    public FirewallRule build() {
        if (!isComplete()) {
            throw new IllegalStateException("A regra precisa de uma Chain e uma Action.");
        }
        return new FirewallRule(chain, protocol, sourceIp, null, port, action);
    }

    public void reset() {
        chain = null;
        protocol = null;
        sourceIp = null;
        port = null;
        action = null;
    }

    /** Resumo legível dos slots preenchidos, para exibição na UI. */
    public String describe() {
        StringBuilder sb = new StringBuilder("Regra atual: ");
        sb.append("chain=").append(chain == null ? "—" : chain);
        sb.append(", proto=").append(protocol == null ? "—" : protocol);
        sb.append(", src=").append(sourceIp == null ? "—" : sourceIp.value());
        sb.append(", porta=").append(port == null ? "—" : port.value());
        sb.append(", action=").append(action == null ? "—" : action);
        return sb.toString();
    }
}
