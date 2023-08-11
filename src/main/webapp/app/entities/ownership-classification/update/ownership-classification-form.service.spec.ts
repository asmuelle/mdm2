import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ownership-classification.test-samples';

import { OwnershipClassificationFormService } from './ownership-classification-form.service';

describe('OwnershipClassification Form Service', () => {
  let service: OwnershipClassificationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OwnershipClassificationFormService);
  });

  describe('Service methods', () => {
    describe('createOwnershipClassificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOwnershipClassificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            ownerships: expect.any(Object),
          })
        );
      });

      it('passing IOwnershipClassification should create a new form with FormGroup', () => {
        const formGroup = service.createOwnershipClassificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            ownerships: expect.any(Object),
          })
        );
      });
    });

    describe('getOwnershipClassification', () => {
      it('should return NewOwnershipClassification for default OwnershipClassification initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOwnershipClassificationFormGroup(sampleWithNewData);

        const ownershipClassification = service.getOwnershipClassification(formGroup) as any;

        expect(ownershipClassification).toMatchObject(sampleWithNewData);
      });

      it('should return NewOwnershipClassification for empty OwnershipClassification initial value', () => {
        const formGroup = service.createOwnershipClassificationFormGroup();

        const ownershipClassification = service.getOwnershipClassification(formGroup) as any;

        expect(ownershipClassification).toMatchObject({});
      });

      it('should return IOwnershipClassification', () => {
        const formGroup = service.createOwnershipClassificationFormGroup(sampleWithRequiredData);

        const ownershipClassification = service.getOwnershipClassification(formGroup) as any;

        expect(ownershipClassification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOwnershipClassification should not enable id FormControl', () => {
        const formGroup = service.createOwnershipClassificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOwnershipClassification should disable id FormControl', () => {
        const formGroup = service.createOwnershipClassificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
