package com.example.training.exception;

/**
 * 研修IDが既に存在する場合にスローされるカスタム例外。
 * RuntimeException を継承することで、非チェック例外として扱われます。
 */
public class TrainingIdAlreadyExistsException extends RuntimeException {

    // コンストラクタ1: エラーメッセージのみを受け取る
    public TrainingIdAlreadyExistsException(String message) {
        super(message); // 親クラス(RuntimeException)のコンストラクタを呼び出す
    }

    // コンストラクタ2: エラーメッセージと原因となった例外を受け取る
    // このコンストラクタは、DuplicateKeyExceptionなどの元の例外をラップする場合に便利です。
    public TrainingIdAlreadyExistsException(String message, Throwable cause) {
        super(message, cause); // 親クラスのコンストラクタを呼び出す
    }
}