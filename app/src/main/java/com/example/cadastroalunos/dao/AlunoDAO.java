package com.example.cadastroalunos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.cadastroalunos.model.Aluno;
import com.example.cadastroalunos.util.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public AlunoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }
    public Aluno obterAlunoPorId(long id) {
        String query = "SELECT * FROM aluno WHERE id = ?";
        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int telefoneIndex = cursor.getColumnIndex("telefone");

            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(idIndex));
            aluno.setNome(cursor.getString(nomeIndex));
            aluno.setCpf(cursor.getString(cpfIndex));
            aluno.setTelefone(cursor.getString(telefoneIndex));

            cursor.close();
            return aluno;
        } else {
            cursor.close();
            return null;
        }
    }


    public long insert(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        return banco.insert("aluno", null, values);
    }

    public void update(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("cpf", aluno.getCpf());

        // Atualiza o registro na tabela onde o ID corresponde ao id do aluno
        banco.update("aluno", values, "id = ?", new String[]{String.valueOf(aluno.getId())});
    }


    public void deleteByCPF(String cpf) {
        banco.delete("aluno", "cpf = ?", new String[] { cpf });
    }

    public Aluno findByNome(String nome) {
        String query = "SELECT * FROM aluno WHERE nome = ?";
        Cursor cursor = banco.rawQuery(query, new String[]{nome});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int telefoneIndex = cursor.getColumnIndex("telefone");

            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(idIndex));
            aluno.setNome(cursor.getString(nomeIndex));
            aluno.setCpf(cursor.getString(cpfIndex));
            aluno.setTelefone(cursor.getString(telefoneIndex));

            cursor.close();
            return aluno;
        } else {
            cursor.close();
            return null;
        }
    }
    public Aluno findByCPF(String cpf) {
        String query = "SELECT * FROM aluno WHERE cpf = ?";
        Cursor cursor = banco.rawQuery(query, new String[]{cpf});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int telefoneIndex = cursor.getColumnIndex("telefone");

            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(idIndex));
            aluno.setNome(cursor.getString(nomeIndex));
            aluno.setCpf(cursor.getString(cpfIndex));
            aluno.setTelefone(cursor.getString(telefoneIndex));

            cursor.close();
            return aluno;
        } else {
            cursor.close();
            return null;
        }
    }
    public Aluno findByTelefone(String telefone) {
        String query = "SELECT * FROM aluno WHERE telefone = ?";
        Cursor cursor = banco.rawQuery(query, new String[]{telefone});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int telefoneIndex = cursor.getColumnIndex("telefone");

            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(idIndex));
            aluno.setNome(cursor.getString(nomeIndex));
            aluno.setCpf(cursor.getString(cpfIndex));
            aluno.setTelefone(cursor.getString(telefoneIndex));

            cursor.close();
            return aluno;
        } else {
            cursor.close();
            return null;
        }
    }




    public List<Aluno> obterTodos() {
        List<Aluno> alunos = new ArrayList<>();
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone"},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int telefoneIndex = cursor.getColumnIndex("telefone");

            do {
                Aluno a = new Aluno();
                a.setId(cursor.getInt(idIndex));
                a.setNome(cursor.getString(nomeIndex));
                a.setCpf(cursor.getString(cpfIndex));
                a.setTelefone(cursor.getString(telefoneIndex));
                alunos.add(a);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return alunos;
    }
}
