import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ownership-property.test-samples';

import { OwnershipPropertyFormService } from './ownership-property-form.service';

describe('OwnershipProperty Form Service', () => {
  let service: OwnershipPropertyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OwnershipPropertyFormService);
  });

  describe('Service methods', () => {
    describe('createOwnershipPropertyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOwnershipPropertyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            type: expect.any(Object),
            ownership: expect.any(Object),
          })
        );
      });

      it('passing IOwnershipProperty should create a new form with FormGroup', () => {
        const formGroup = service.createOwnershipPropertyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            type: expect.any(Object),
            ownership: expect.any(Object),
          })
        );
      });
    });

    describe('getOwnershipProperty', () => {
      it('should return NewOwnershipProperty for default OwnershipProperty initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOwnershipPropertyFormGroup(sampleWithNewData);

        const ownershipProperty = service.getOwnershipProperty(formGroup) as any;

        expect(ownershipProperty).toMatchObject(sampleWithNewData);
      });

      it('should return NewOwnershipProperty for empty OwnershipProperty initial value', () => {
        const formGroup = service.createOwnershipPropertyFormGroup();

        const ownershipProperty = service.getOwnershipProperty(formGroup) as any;

        expect(ownershipProperty).toMatchObject({});
      });

      it('should return IOwnershipProperty', () => {
        const formGroup = service.createOwnershipPropertyFormGroup(sampleWithRequiredData);

        const ownershipProperty = service.getOwnershipProperty(formGroup) as any;

        expect(ownershipProperty).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOwnershipProperty should not enable id FormControl', () => {
        const formGroup = service.createOwnershipPropertyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOwnershipProperty should disable id FormControl', () => {
        const formGroup = service.createOwnershipPropertyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
