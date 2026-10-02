import model.Aluno;
import model.Curso;
import model.Instrumento;
import model.Matricula;
import model.Professor;

// Classe de demonstracao: mostra no console como as classes se relacionam.
// Nao usa banco de dados nem telas (e so para explicar o fluxo do sistema).
public class Demonstracao {

    public static void main(String[] args) {

        // Instrumento -> e usado em -> Curso
        Instrumento violao = new Instrumento(1, "Violao", "CORDAS");

        // Professor -> ministra -> Curso
        Professor professor = new Professor(1, "Carlos Mendes", "(51) 99999-0001", "Violao e Guitarra");
        Curso curso = new Curso(1, "Violao Iniciante", 150.00, "Segunda 14:00",
                violao.getId(), professor.getId());

        // Aluno -> realiza -> Matricula -> pertence a -> Curso
        Aluno aluno = new Aluno(1, "Maria Silva", 15, "(51) 99999-0002", "maria@email.com");
        Matricula matricula = new Matricula(1, aluno.getId(), curso.getId());

        System.out.println("=== Sistema de Escola de Musica ===");
        System.out.println("Instrumento: " + violao + " (" + violao.getTipo() + ")");
        System.out.println("Professor: " + professor);
        System.out.println("Curso: " + curso + " | " + curso.getDiaHorario()
                + " | R$ " + curso.getValorMensalidade());
        System.out.println("Aluno: " + aluno + " | Menor de idade? " + aluno.ehMenorDeIdade());
        System.out.println("Matricula " + matricula.getId() + " | Aluno " + matricula.getIdAluno()
                + " no Curso " + matricula.getIdCurso() + " | Status: " + matricula.getStatus());
        System.out.println("Valor do curso em 6 meses: R$ " + curso.calcularValorTotal(6));

        matricula.trancar();
        System.out.println("Depois de trancar, o status e: " + matricula.getStatus());
    }
}
