import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../owner.test-samples';

import { OwnerFormService } from './owner-form.service';

describe('Owner Form Service', () => {
  let service: OwnerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OwnerFormService);
  });

  describe('Service methods', () => {
    describe('createOwnerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOwnerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fullName: expect.any(Object),
            ownerKey: expect.any(Object),
            ownerGroup: expect.any(Object),
            meters: expect.any(Object),
            lastWeek: expect.any(Object),
            beforeLastWeek: expect.any(Object),
            amr: expect.any(Object),
            lastYear: expect.any(Object),
            contactEmail: expect.any(Object),
            electricityPrice: expect.any(Object),
            gasPrice: expect.any(Object),
            gasStage: expect.any(Object),
            electricityStage: expect.any(Object),
            waterStage: expect.any(Object),
            heatStage: expect.any(Object),
            solarHeat: expect.any(Object),
            solarPowerStage: expect.any(Object),
            windStage: expect.any(Object),
            cogenPowerStage: expect.any(Object),
          })
        );
      });

      it('passing IOwner should create a new form with FormGroup', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fullName: expect.any(Object),
            ownerKey: expect.any(Object),
            ownerGroup: expect.any(Object),
            meters: expect.any(Object),
            lastWeek: expect.any(Object),
            beforeLastWeek: expect.any(Object),
            amr: expect.any(Object),
            lastYear: expect.any(Object),
            contactEmail: expect.any(Object),
            electricityPrice: expect.any(Object),
            gasPrice: expect.any(Object),
            gasStage: expect.any(Object),
            electricityStage: expect.any(Object),
            waterStage: expect.any(Object),
            heatStage: expect.any(Object),
            solarHeat: expect.any(Object),
            solarPowerStage: expect.any(Object),
            windStage: expect.any(Object),
            cogenPowerStage: expect.any(Object),
          })
        );
      });
    });

    describe('getOwner', () => {
      it('should return NewOwner for default Owner initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOwnerFormGroup(sampleWithNewData);

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject(sampleWithNewData);
      });

      it('should return NewOwner for empty Owner initial value', () => {
        const formGroup = service.createOwnerFormGroup();

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject({});
      });

      it('should return IOwner', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOwner should not enable id FormControl', () => {
        const formGroup = service.createOwnerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOwner should disable id FormControl', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
