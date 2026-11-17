import java.io.IOException;

class ExamException extends Exception {
    ExamException(String message) { super(message); }
    ExamException(String message, Throwable cause) { super(message, cause); }
}

class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ExamWindowClosedException extends ExamException {
    ExamWindowClosedException(String message) { super(message); }
}

class NotRegisteredException extends ExamException {
    NotRegisteredException(String message) { super(message); }
}

class FileSizeLimitException extends ExamException {
    FileSizeLimitException(String message) { super(message); }
}

class Storage {
    void save(String fileName, long sizeKb) throws SubmissionStorageException {
        try {
            if (sizeKb > 500) throw new IOException("Disk quota exceeded");
            System.out.println("Saved " + fileName);
        } catch (IOException e) {
            // TODO 1 completed: preserve the original IOException as the cause.
            throw new SubmissionStorageException("Could not store " + fileName, e);
        }
    }
}

class ExamService {
    private final Storage storage = new Storage();

    void submit(String fileName, long sizeKb, boolean registered, boolean examOpen)
            throws ExamException {
        if (!examOpen) throw new ExamWindowClosedException("Exam window has closed");
        if (!registered) throw new NotRegisteredException("Student is not registered");
        if (sizeKb > 500) {
            // The storage layer performs the final storage check and chains IOException.
            storage.save(fileName, sizeKb);
            return;
        }
        storage.save(fileName, sizeKb);
    }
}

class ExamController {
    private final ExamService service = new ExamService();

    void submitRequest(String file, long size, boolean registered, boolean open) {
        try {
            service.submit(file, size, registered, open);
            System.out.println("Submission accepted");
        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null)
                System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}

public class StorageDemo {
    public static void main(String[] args) {
        Storage storage = new Storage();
        try {
            storage.save("answer01.pdf", 120);
            storage.save("answer02.pdf", 800);
        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        System.out.println("\nAdditional portal scenarios:");
        ExamController controller = new ExamController();
        controller.submitRequest("answer03.pdf", 100, true, false);
        controller.submitRequest("answer04.pdf", 100, false, true);
        controller.submitRequest("answer05.pdf", 700, true, true);
    }
}
