package gift.exception;

public class ProductExceptions {
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(Long productId) { super("해당 상품이 존재하지 않습니다: " + productId); }
    }
}
