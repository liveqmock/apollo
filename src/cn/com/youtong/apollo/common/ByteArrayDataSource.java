package cn.com.youtong.apollo.common;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.activation.DataSource;

public class ByteArrayDataSource
    implements DataSource, Serializable {

  private byte[] _bytes;
  private String _contentType;
  private String _name;

  public ByteArrayDataSource(byte[] bytes, String contentType, String name) {
    _bytes = bytes;

    if (contentType == null) {
      _contentType = "application/octet-stream";
    }
    else {
      _contentType = contentType;
      _name = name;
    }
  }

  public byte[] getBytes() {
    return _bytes;
  }

  public String getContentType() {
    return _contentType;
  }

  public InputStream getInputStream() {
    return new ByteArrayInputStream(_bytes, 0, _bytes.length - 2);
  }

  public String getName() {
    return _name;
  }

  public OutputStream getOutputStream() throws IOException {
    throw new FileNotFoundException();
  }

}
