package br.codingdojo.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import br.codingdojo.model.Desafio;
import br.codingdojo.model.Sessao;
import org.jetbrains.annotations.NotNull;
import br.codingdojo.repository.DesafioRepository;
import br.codingdojo.repository.SessaoRepository;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * UC01 - Configurar Sessão de Dojo.
 *
 * Tool Window (lateral da IDE) onde o professor:
 *  - seleciona um desafio pré-cadastrado (JComboBox);
 *  - define a duração de cada rodada em minutos (JSpinner);
 *  - inicia a sessão (JButton).
 */
public class DojoConfigToolwindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel panel = buildPanel(project);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private JPanel buildPanel(Project project) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;

        // --- Regra 2: seleção do desafio ---------------------------------
        c.gridy = 0;
        panel.add(new JLabel("Desafio:"), c);

        DesafioRepository desafioRepository = new DesafioRepository();
        List<Desafio> desafios = desafioRepository.listarDesafios();

        c.weightx = 1.0;
                JComboBox<Desafio> comboDesafios = new JComboBox<>(new DefaultComboBoxModel<>());
                for (Desafio desafio : desafios) {
                    comboDesafios.addItem(desafio);
                }

                c.gridy = 1;
                panel.add(comboDesafios, c);
                c.weightx = 0.0;

        // --- Regra 3: duração da rodada em minutos -----------------------
        c.gridy = 2;
        panel.add(new JLabel("Duração da rodada (min):"), c);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(5, 1, 120, 1);
        JSpinner spinnerDuracao = new JSpinner(spinnerModel);

        c.gridy = 3;
        panel.add(spinnerDuracao, c);

        // --- Regra 4: inicializar sessão ----------------------------------
        JButton botaoIniciar = new JButton("Iniciar Sessão");
        c.gridy = 4;
        c.insets = new Insets(16, 4, 4, 4);
        panel.add(botaoIniciar, c);

        botaoIniciar.addActionListener(e -> {
            Desafio desafioSelecionado = (Desafio) comboDesafios.getSelectedItem();

            if (desafioSelecionado == null) {
                Messages.showErrorDialog(
                        project,
                        "Selecione um desafio antes de iniciar a sessão.",
                        "Configuração Incompleta"
                );
                return;
            }

            int duracaoRodada = (Integer) spinnerDuracao.getValue();

            // Registra a Sessão vinculada ao desafio escolhido. A coluna "data"
            // tem default CURRENT_TIMESTAMP no schema, então não a preenchemos aqui.
            Sessao novaSessao = new Sessao();
            novaSessao.setIdDesafio(desafioSelecionado.getId());

            SessaoRepository sessaoRepository = new SessaoRepository();
            int idSessaoGerado = sessaoRepository.registrar(novaSessao);

            if (idSessaoGerado == -1) {
                Messages.showErrorDialog(
                        project,
                        "Não foi possível registrar a sessão no banco de dados.",
                        "Erro ao Iniciar Sessão"
                );
                return;
            }

            // A duração da rodada não é persistida aqui: o schema atual não tem
            // uma coluna para isso em "Sessao" nem em "Rodada" (que é por
            // participante, preenchida apenas durante a execução da prática).
            // Repassamos o valor para o controlador da sessão em execução, que
            // usará essa duração para o timer de cada rodada.
            DojoSessionRunTime.iniciar(idSessaoGerado, duracaoRodada);

            Messages.showInfoMessage(
                    project,
                    "Sessão configurada com sucesso!\n\n"
                            + "Desafio: " + desafioSelecionado.getTitulo() + "\n"
                            + "Duração de cada rodada: " + duracaoRodada + " min",
                    "Sessão de Dojo Iniciada"
            );
        });

        return panel;
    }
}
