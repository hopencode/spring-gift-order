package gift.exception;

public class ProductOptionExceptions {
    public static class OptionAndProductMismatchException extends RuntimeException {
        public OptionAndProductMismatchException(String productName, String productOptionName) { super(productName + " 상품에 " + productOptionName + " 옵션이 없습니다."); }
    }

    public static class DuplicateOptionException extends RuntimeException {
        public DuplicateOptionException(Long productId) { super(productId + "에 이미 존재하는 옵션입니다."); }
    }
}
