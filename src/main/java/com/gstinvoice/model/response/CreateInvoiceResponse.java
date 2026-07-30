package com.gstinvoice.model.response;

public class CreateInvoiceResponse {

	private Long id;
    private String invoice_number;

    public CreateInvoiceResponse(Long id, String invoice_number) {
        this.id = id;
        this.invoice_number = invoice_number;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}
    
}
