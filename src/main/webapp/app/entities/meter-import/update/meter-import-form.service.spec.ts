import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../meter-import.test-samples';

import { MeterImportFormService } from './meter-import-form.service';

describe('MeterImport Form Service', () => {
  let service: MeterImportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MeterImportFormService);
  });

  describe('Service methods', () => {
    describe('createMeterImportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMeterImportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            provider: expect.any(Object),
            utility: expect.any(Object),
            namespace: expect.any(Object),
            clientRef: expect.any(Object),
            meterName: expect.any(Object),
            contactEmail: expect.any(Object),
            ownership: expect.any(Object),
            owner: expect.any(Object),
            postcode: expect.any(Object),
            addresslines: expect.any(Object),
            lat: expect.any(Object),
            lon: expect.any(Object),
            classifications: expect.any(Object),
          })
        );
      });

      it('passing IMeterImport should create a new form with FormGroup', () => {
        const formGroup = service.createMeterImportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            provider: expect.any(Object),
            utility: expect.any(Object),
            namespace: expect.any(Object),
            clientRef: expect.any(Object),
            meterName: expect.any(Object),
            contactEmail: expect.any(Object),
            ownership: expect.any(Object),
            owner: expect.any(Object),
            postcode: expect.any(Object),
            addresslines: expect.any(Object),
            lat: expect.any(Object),
            lon: expect.any(Object),
            classifications: expect.any(Object),
          })
        );
      });
    });

    describe('getMeterImport', () => {
      it('should return NewMeterImport for default MeterImport initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMeterImportFormGroup(sampleWithNewData);

        const meterImport = service.getMeterImport(formGroup) as any;

        expect(meterImport).toMatchObject(sampleWithNewData);
      });

      it('should return NewMeterImport for empty MeterImport initial value', () => {
        const formGroup = service.createMeterImportFormGroup();

        const meterImport = service.getMeterImport(formGroup) as any;

        expect(meterImport).toMatchObject({});
      });

      it('should return IMeterImport', () => {
        const formGroup = service.createMeterImportFormGroup(sampleWithRequiredData);

        const meterImport = service.getMeterImport(formGroup) as any;

        expect(meterImport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMeterImport should not enable id FormControl', () => {
        const formGroup = service.createMeterImportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMeterImport should disable id FormControl', () => {
        const formGroup = service.createMeterImportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
