package com.example.cadastroalunos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cadastroalunos.dao.AlunoDAO;
import com.example.cadastroalunos.model.Aluno;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button buttonMenu;
    private AlunoDAO alunoDAO;
    private List<Aluno> alunos;
    private ArrayAdapter<Aluno> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        buttonMenu = findViewById(R.id.buttonMenu);
        alunoDAO = new AlunoDAO(this);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Crud.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long alunoId = alunos.get(position).getId(); // Obtém o ID do aluno clicado

                // Crie uma Intent para a tela Crud
                Intent crudIntent = new Intent(MainActivity.this, Crud.class);
                crudIntent.putExtra("alunoId", alunoId); // Passa o ID do aluno clicado para a próxima tela
                startActivity(crudIntent); // Inicia a tela Crud
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        alunos = alunoDAO.obterTodos();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listView.setAdapter(adapter);
    }
}
