package bitcrownd.wallet.bitcrownd.model;

/**
 * Represents an exception thrown by the Coinprism API.
 */
public class APIException extends Exception
{
    private final String errorCode;
    private final String subCode;

    public APIException(String errorCode, String subCode)
    {
        this.errorCode = errorCode;
        this.subCode = subCode;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getSubCode()
    {
        return subCode;
    }
}
