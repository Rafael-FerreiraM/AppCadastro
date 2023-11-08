package com.example.cadastroalunos;
import static android.icu.text.DisplayContext.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import com.example.cadastroalunos.dao.AlunoDAO;
import com.example.cadastroalunos.model.Aluno;


public class Crud extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextCPF;
    private EditText editTextTelefone;
    private Button buttonGravar;
    private Button buttonAlterar;
    private Button buttonExcluir;
    private Button buttonVoltar;
    private Button buttonPdf;
    private AlunoDAO alunoDAO;
    private long alunoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        alunoDAO = new AlunoDAO(this);

        editTextNome = findViewById(R.id.editTextNome);
        editTextCPF = findViewById(R.id.editTextCPF);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        buttonGravar = findViewById(R.id.buttonGravar);
        buttonAlterar = findViewById(R.id.buttonAlterar);
        buttonExcluir = findViewById(R.id.buttonExcluir);
        buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonPdf = findViewById(R.id.buttonPdf);

        // Obtem o ID do aluno selecionado da MainActivity
        alunoId = getIntent().getLongExtra("alunoId", -1);

        if (alunoId != -1) {
            // carrega os detalhes do aluno
            Aluno alunoSelecionado = alunoDAO.obterAlunoPorId(alunoId);
            if (alunoSelecionado != null) {
                // Preenche os campos com os detalhes do aluno selecionado
                editTextNome.setText(alunoSelecionado.getNome());
                editTextCPF.setText(alunoSelecionado.getCpf());
                editTextTelefone.setText(alunoSelecionado.getTelefone());

                // Desabilita o botão "Gravar" porque um aluno foi selecionado
                buttonGravar.setEnabled(false);

                // Habilitar os botões de "Alterar" e "Excluir" se um aluno for selecionado
                buttonAlterar.setEnabled(true);
                buttonExcluir.setEnabled(true);
            }
        } else {
            // Habilitar o botão "Gravar" se nenhum aluno for selecionado
            buttonGravar.setEnabled(true);

            // Desabilitar os botões de "Alterar" e "Excluir" se nenhum aluno for selecionado
            buttonAlterar.setEnabled(false);
            buttonExcluir.setEnabled(false);
        }

        // TextWatchers para os campos
        editTextNome.addTextChangedListener(textWatcher);
        editTextCPF.addTextChangedListener(textWatcher);
        editTextTelefone.addTextChangedListener(textWatcher);

        // OnEditorActionListener para o campo Telefone
        editTextTelefone.setOnEditorActionListener(editorActionListener);

        buttonGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravarAluno();
            }
        });

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarAluno();
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirAluno();
            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        buttonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarPdf();
            }
        });
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Única maneira de definir ícones na opção do menu com action "never"
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1) {
            // Verifica se o aluno já existe com base no CPF
            String cpf = editTextCPF.getText().toString();
            Aluno cpfExistente = alunoDAO.findByCPF(cpf);
            if (cpfExistente != null) {
                // Aluno com o mesmo CPF já existe, mostre uma mensagem de erro
                Toast.makeText(this, "Já existe um aluno com esses dados.", Toast.LENGTH_SHORT).show();
            } else {
                // Aluno não existe, continue com a gravação
                gravarAluno();
            }
            return true;
        } else if (id == R.id.item2) {
            if (alunoId != -1) {
                // Verifica se um aluno foi selecionado na MainActivity
                alterarAluno();
            } else {
                Toast.makeText(this, "Selecione um aluno da lista para alterar.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.item3) {
            if (alunoId != -1) {
                // Verifica se um aluno foi selecionado na MainActivity
                excluirAluno();
            } else {
                Toast.makeText(this, "Selecione um aluno da lista para excluir.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.item4) {
            voltar();
            return true;
        } else if (id == R.id.item5) {
            gerarPdf();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TextWatcher para habilitar o botão de Alterar
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            enableAlterarButton();
        }
    };

    // Listener para a tecla "Done" no teclado
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                gravarAluno();
                return true;
            }
            return false;
        }
    };

    // Método para habilitar/desabilitar o botão de Alterar
    private void enableAlterarButton() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String telefone = editTextTelefone.getText().toString();

        if (!nome.isEmpty() && !cpf.isEmpty() && !telefone.isEmpty()) {
            buttonAlterar.setEnabled(true);
        } else {
            buttonAlterar.setEnabled(false);
        }
    }

    private void gravarAluno() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String telefone = editTextTelefone.getText().toString();

        if (isNumeric(cpf) && isNumeric(telefone) && isAlphabetic(nome)) {
            Aluno cpfExistente = alunoDAO.findByCPF(cpf);
            Aluno telefoneExistente = alunoDAO.findByTelefone(telefone);

            if (cpfExistente == null) {
                if (telefoneExistente == null) {
                    // Não existe um aluno com o mesmo CPF e telefone
                    Aluno novoAluno = new Aluno(null, nome, cpf, telefone);
                    long resultado = alunoDAO.insert(novoAluno);

                    if (resultado != -1) {
                        // Gravação bem-sucedida, obtem o ID do novo aluno
                        Aluno alunoInserido = alunoDAO.findByCPF(cpf);
                        long alunoId = alunoInserido.getId();

                        Toast.makeText(this, "Aluno gravado com sucesso!", Toast.LENGTH_SHORT).show();
                        limparCampos();

                        // Cria uma Intent para a tela MainActivity
                        Intent mainIntent = new Intent(Crud.this, MainActivity.class);

                        // Passa o ID do novo aluno como um extra na Intent
                        mainIntent.putExtra("alunoId", alunoId);

                        // Inicia a tela MainActivity
                        startActivity(mainIntent);
                    }
                } else {
                    // Telefone já existe, mostre uma mensagem de erro
                    Toast.makeText(this, "Já existe um aluno com esse telefone.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // CPF já existe, mostre uma mensagem de erro
                Toast.makeText(this, "Já existe um aluno com esse CPF.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "CPF deve conter apenas números, telefone deve conter apenas números e nome deve conter apenas letras.", Toast.LENGTH_SHORT).show();
        }
    }

    private void alterarAluno() {
        String nome = editTextNome.getText().toString();
        String novoCPF = editTextCPF.getText().toString();
        String telefone = editTextTelefone.getText().toString();

        if (isNumeric(novoCPF) && isNumeric(telefone) && isAlphabetic(nome) && alunoId != -1) {
            // Verifica se um aluno foi selecionado na MainActivity
            if (alunoId != -1) {
                // Obtém o aluno existente no banco de dados com base no ID
                Aluno alunoExistente = alunoDAO.obterAlunoPorId(alunoId);

                // Verifica se o CPF inserido é igual ao CPF existente no banco de dados
                if (alunoExistente != null && !novoCPF.equals(alunoExistente.getCpf())) {
                    // O CPF foi alterado, verifica se não existe outro aluno com o mesmo CPF
                    Aluno cpfExistente = alunoDAO.findByCPF(novoCPF);

                    if (cpfExistente == null) {
                        // Verifica se o telefone inserido é igual ao telefone existente no banco de dados
                        if (!telefone.equals(alunoExistente.getTelefone())) {
                            // O telefone foi alterado, verifica se não existe outro aluno com o mesmo telefone
                            Aluno telefoneExistente = alunoDAO.findByTelefone(telefone);

                            if (telefoneExistente == null) {
                                // Não existe um aluno com o mesmo CPF e telefone, então pode atualizar o aluno
                                Aluno alunoAtualizado = new Aluno((int) alunoId, nome, novoCPF, telefone);

                                // Atualiza o aluno no banco de dados
                                alunoDAO.update(alunoAtualizado);

                                Toast.makeText(this, "Aluno alterado com sucesso!", Toast.LENGTH_SHORT).show();
                                limparCampos();
                            } else {
                                // Telefone já existe, mostre uma mensagem de erro
                                Toast.makeText(this, "Já existe um aluno com esse telefone.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // CPF foi alterado, mas o telefone permanece o mesmo, apenas atualiza os outros campos
                            Aluno alunoAtualizado = new Aluno((int) alunoId, nome, novoCPF, telefone);
                            alunoDAO.update(alunoAtualizado);

                            Toast.makeText(this, "Aluno alterado com sucesso!", Toast.LENGTH_SHORT).show();
                            limparCampos();
                        }
                    } else {
                        // CPF já existe, mostre uma mensagem de erro
                        Toast.makeText(this, "Já existe um aluno com esse CPF.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // CPF não foi alterado, apenas atualiza os outros campos
                    Aluno alunoAtualizado = new Aluno((int) alunoId, nome, novoCPF, telefone);
                    alunoDAO.update(alunoAtualizado);

                    Toast.makeText(this, "Aluno alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                }
            } else {
                Toast.makeText(this, "Selecione um aluno da lista para alterar.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "CPF deve conter apenas números, telefone deve conter apenas números e nome deve conter apenas letras.", Toast.LENGTH_SHORT).show();
        }
    }


    private void excluirAluno() {
        String cpf = editTextCPF.getText().toString();

        if (!cpf.isEmpty()) {
            Aluno alunoParaExcluir = alunoDAO.findByCPF(cpf);

            if (alunoParaExcluir != null) {
                alunoDAO.deleteByCPF(cpf);
                Toast.makeText(this, "Aluno excluído com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
            } else {
                Toast.makeText(this, "Selecione um Aluno da lista para excluir.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Gerar PDF
    private void gerarPdf() {
        Intent intent = new Intent(Crud.this, PdfActivity.class);
        startActivity(intent);
    }

    // Voltar
    private void voltar() {
        finish();
    }
    private void limparCampos() {
        editTextNome.setText("");
        editTextCPF.setText("");
        editTextTelefone.setText("");
    }
    private boolean isNumeric(String input) {
        // Verifica se a entrada contém apenas dígitos
        return input.matches("\\d*");
    }

    private boolean isAlphabetic(String input) {
        // Verifica se a entrada contém apenas letras
        return input.matches("[a-zA-Z]+");
    }
}

