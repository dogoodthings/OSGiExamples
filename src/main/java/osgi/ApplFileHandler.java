package osgi;

import java.util.Collection;
import java.util.stream.Collectors;

import com.dscsag.plm.spi.interfaces.DocumentInfo;
import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.applfilehandling.ApplFileInfo;
import com.dscsag.plm.spi.interfaces.applfilehandling.ApplicationFileHandler;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

/**
 * file handler which is called every time when files are provided in ECTR
 * session directory (but only for the files of the documents with application
 * type UGS) this handler does nothing besides just log all the files which are
 * provided or removed by ECTR into ECTR log file
 */
public class ApplFileHandler implements ApplicationFileHandler
{
  private ECTRService service;

  public ApplFileHandler(ECTRService service)
  {
    this.service = service;
  }

  @Override
  public String getApplType()
  {
    return "UGS"; // this application type is for NX documents
  }

  @Override
  public void filesProvided(Collection<ApplFileInfo> applFiles)
  {
    // just log all the files
    PlmLogger plmLogger = service.getPlmLogger();
    plmLogger.trace("java::ApplFileHandler.filesProvided(...)");
    applFiles.stream().map(ApplFileHandler::applFileInfoToString).forEach(plmLogger::verbose);
    long uptodateCount = applFiles.stream().filter(x -> x.wasUpToDate()).collect(Collectors.counting());
    plmLogger
        .trace("java::ApplFileHandler.filesProvided(): " + uptodateCount + " of " + applFiles.size()
            + " was uptodate in session.");
    plmLogger.trace("java::ApplFileHandler.filesProvided(.)");
  }

  @Override
  public void filesRemoved(Collection<ApplFileInfo> applFiles)
  {
    // just log all the files
    PlmLogger plmLogger = service.getPlmLogger();
    plmLogger.trace("java::ApplFileHandler.filesRemoved(...)");
    applFiles.stream().map(ApplFileHandler::applFileInfoToString).forEach(plmLogger::verbose);
    plmLogger.trace("java::ApplFileHandler.filesRemoved(.)");
  }

  private static String applFileInfoToString(ApplFileInfo x)
  {
    DocumentInfo docInfo = x.getDocumentInfo();
    return "| " + docInfo.getApplType() + " | " + docInfo.getDTypeId() + " | "
        + docInfo.getUserDefined4() + " | " + docInfo.getType() + " | "
        + docInfo.getNumber() + " | " + docInfo.getPart() + " | "
        + docInfo.getVersion() + " | " + docInfo.getSapKey() + " | " + x.getMasterFile() + " | "
        + x.getAdditionalDirectory() + " |";
  }

}