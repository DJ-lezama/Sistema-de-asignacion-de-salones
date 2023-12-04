public enum EdificiosEnum {
    IA, CL, CN, NE, SL, CS, HU;

    public static EdificiosEnum Ed2Int(int num)
    {
        switch (num)
        {
            case 0:
                return IA;
            case 1:
                return CL;
            case 2:
                return CN;
            case 3:
                return NE;
            case 4:
                return SL;
            case 5:
                return CS;
            case 6:
                return HU;
            default:
                return null;
        }
    }
}