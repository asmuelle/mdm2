import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../waste-tracking-parameters.test-samples';

import { WasteTrackingParametersFormService } from './waste-tracking-parameters-form.service';

describe('WasteTrackingParameters Form Service', () => {
  let service: WasteTrackingParametersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WasteTrackingParametersFormService);
  });

  describe('Service methods', () => {
    describe('createWasteTrackingParametersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            wasteIssueCreationThreshold: expect.any(Object),
            maxWasteIssueCreationRate: expect.any(Object),
            maxActiveWasteIssues: expect.any(Object),
            autoCreateWasteIssues: expect.any(Object),
          })
        );
      });

      it('passing IWasteTrackingParameters should create a new form with FormGroup', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            wasteIssueCreationThreshold: expect.any(Object),
            maxWasteIssueCreationRate: expect.any(Object),
            maxActiveWasteIssues: expect.any(Object),
            autoCreateWasteIssues: expect.any(Object),
          })
        );
      });
    });

    describe('getWasteTrackingParameters', () => {
      it('should return NewWasteTrackingParameters for default WasteTrackingParameters initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWasteTrackingParametersFormGroup(sampleWithNewData);

        const wasteTrackingParameters = service.getWasteTrackingParameters(formGroup) as any;

        expect(wasteTrackingParameters).toMatchObject(sampleWithNewData);
      });

      it('should return NewWasteTrackingParameters for empty WasteTrackingParameters initial value', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup();

        const wasteTrackingParameters = service.getWasteTrackingParameters(formGroup) as any;

        expect(wasteTrackingParameters).toMatchObject({});
      });

      it('should return IWasteTrackingParameters', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup(sampleWithRequiredData);

        const wasteTrackingParameters = service.getWasteTrackingParameters(formGroup) as any;

        expect(wasteTrackingParameters).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWasteTrackingParameters should not enable id FormControl', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWasteTrackingParameters should disable id FormControl', () => {
        const formGroup = service.createWasteTrackingParametersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
